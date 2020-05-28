package com.khdv.habitstracker.data.repositories

import com.khdv.habitstracker.data.db.HabitDao
import com.khdv.habitstracker.data.db.HabitWithRepetitions
import com.khdv.habitstracker.data.mappers.toDto
import com.khdv.habitstracker.data.mappers.toEntity
import com.khdv.habitstracker.data.mappers.toException
import com.khdv.habitstracker.data.mappers.toModel
import com.khdv.habitstracker.data.network.HabitDto
import com.khdv.habitstracker.data.network.HabitUidDto
import com.khdv.habitstracker.data.network.HabitsService
import com.khdv.habitstracker.data.network.error
import com.khdv.habitstracker.domain.interactors.HabitsRepository
import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.domain.shared.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitsRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val habitsService: HabitsService
) : HabitsRepository {

    @ExperimentalCoroutinesApi
    override fun getAll() = flow {
        val cachedHabits = habitDao.getAll().map {
            Result.Success(it.map(HabitWithRepetitions::toModel))
        }
        emit(cachedHabits.first())
        val result = refreshHabits()
        if (result is Result.Error<*>) {
            @Suppress("UNCHECKED_CAST")
            emit(result as Result<List<Habit>>)
        }
        emitAll(cachedHabits)
    }

    override suspend fun getById(id: String) = withContext(Dispatchers.IO) {
        habitDao.getById(id).toModel()
    }

    override suspend fun insert(habit: Habit): Result<*> = withContext(Dispatchers.IO) {
        performSafely({ insert(habit) }) {
            habitsService.updateHabit(habit.toDto())
        }.also {
            if (it is Result.Success) {
                val id = it.data.uid
                habitDao.insert(habit.copy(id = id).toEntity())
            }
        }
    }

    override suspend fun update(habit: Habit): Result<*> = withContext(Dispatchers.IO) {
        performSafely({ update(habit) }) {
            habitsService.updateHabit(habit.toDto())
        }.also {
            if (it is Result.Success)
                habitDao.update(habit.toEntity())
        }
    }

    override suspend fun delete(habit: Habit): Result<*> = withContext(Dispatchers.IO) {
        performSafely({ delete(habit) }) {
            habitsService.deleteHabit(HabitUidDto(habit.id))
        }.also {
            if (it is Result.Success)
                habitDao.delete(habit.toEntity())
        }
    }

    private suspend fun refreshHabits(): Result<Unit> = performSafely(this::refreshHabits) {
        val habits = habitsService.getHabits().map(HabitDto::toEntity)
        habitDao.updateAll(habits)
    }

    private suspend fun <T, R> performSafely(
        retry: suspend () -> Result<T>,
        call: suspend () -> R
    ): Result<R> = try {
        Result.Success(call.invoke())
    } catch (exception: HttpException) {
        val error = exception.response()?.error()
        Result.Error(error?.toException() ?: exception, retry)
    } catch (exception: Exception) {
        Result.Error(exception, retry)
    }
}