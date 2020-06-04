package com.khdv.habitstracker.data.repositories

import com.khdv.habitstracker.data.db.HabitDao
import com.khdv.habitstracker.data.db.HabitWithRepetitions
import com.khdv.habitstracker.data.mappers.toDto
import com.khdv.habitstracker.data.mappers.toEntity
import com.khdv.habitstracker.data.mappers.toModel
import com.khdv.habitstracker.data.network.HabitDto
import com.khdv.habitstracker.data.network.HabitUidDto
import com.khdv.habitstracker.data.network.HabitsService
import com.khdv.habitstracker.data.utils.performSafely
import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.domain.repositories.HabitsRepository
import com.khdv.habitstracker.domain.shared.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitsRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val habitsService: HabitsService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
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

    override suspend fun getById(id: String) = withContext(ioDispatcher) {
        habitDao.getById(id).toModel()
    }

    override suspend fun insert(habit: Habit): Result<*> = withContext(ioDispatcher) {
        performSafely({ insert(habit) }) {
            habitsService.updateHabit(habit.toDto())
        }.also {
            if (it is Result.Success) {
                val id = it.data.uid
                habitDao.insert(habit.copy(id = id).toEntity())
            }
        }
    }

    override suspend fun update(habit: Habit): Result<*> = withContext(ioDispatcher) {
        performSafely({ update(habit) }) {
            habitsService.updateHabit(habit.toDto())
        }.also {
            if (it is Result.Success)
                habitDao.update(habit.toEntity())
        }
    }

    override suspend fun delete(habit: Habit): Result<*> = withContext(ioDispatcher) {
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
}