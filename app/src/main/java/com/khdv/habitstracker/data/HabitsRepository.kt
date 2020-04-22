package com.khdv.habitstracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.khdv.habitstracker.db.HabitDao
import com.khdv.habitstracker.db.HabitEntity
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.network.HabitDto
import com.khdv.habitstracker.network.HabitsService
import com.khdv.habitstracker.network.error
import com.khdv.habitstracker.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HabitsRepository(private val habitDao: HabitDao, private val habitsService: HabitsService) {

    fun getAll(): LiveData<Result<List<Habit>>> = liveData(Dispatchers.IO) {
        val cachedHabits: LiveData<Result<List<Habit>>> = habitDao.getAll().map {
            Result.Success(it.map(HabitEntity::toModel))
        }
        emitSource(cachedHabits)
        val result = makeSafetyApiCall {
            val habits = habitsService.getHabits().map(HabitDto::toEntity)
            habitDao.insertAll(habits)
        }
        if (result is Result.Error) {
            emit(result)
            emitSource(cachedHabits)
        }
    }

    suspend fun getById(id: String) = withContext(Dispatchers.IO) {
        habitDao.getById(id).toModel()
    }

    suspend fun insert(habit: Habit) = withContext(Dispatchers.IO) {
        makeSafetyApiCall {
            habitsService.updateHabit(habit.toDto())
        }.also {
            if (it is Result.Success) {
                val id = it.data.uid
                habitDao.insert(habit.copy(id = id).toEntity())
            }
        }
    }

    suspend fun update(habit: Habit) = withContext(Dispatchers.IO) {
        makeSafetyApiCall {
            habitsService.updateHabit(habit.toDto())
        }.also {
            if (it is Result.Success)
                habitDao.update(habit.toEntity())
        }
    }

    private suspend fun <T> makeSafetyApiCall(call: suspend () -> T) = try {
        Result.Success(call.invoke())
    } catch (exception: HttpException) {
        val error = exception.response()?.error()
        Result.Error(error?.toException() ?: exception)
    } catch (exception: Exception) {
        Result.Error(exception)
    }
}