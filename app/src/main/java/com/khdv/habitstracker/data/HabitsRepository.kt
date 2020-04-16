package com.khdv.habitstracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.khdv.habitstracker.db.HabitDao
import com.khdv.habitstracker.db.HabitEntity
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.network.HabitDto
import com.khdv.habitstracker.network.HabitsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitsRepository(private val habitDao: HabitDao, private val habitsService: HabitsService) {

    fun getAll(): LiveData<List<Habit>> = liveData(Dispatchers.IO) {
        emitSource(habitDao.getAll().map { it.map(HabitEntity::toModel) })
        val habits = habitsService.getHabits().map(HabitDto::toEntity)
        habitDao.insertAll(habits)
    }

    suspend fun getById(id: String) = withContext(Dispatchers.IO) {
        habitDao.getById(id).toModel()
    }

    suspend fun insert(habit: Habit) = withContext(Dispatchers.IO) {
        val id = habitsService.updateHabit(habit.toDto()).uid
        habitDao.insert(habit.copy(id = id).toEntity())
    }

    suspend fun update(habit: Habit) = withContext(Dispatchers.IO) {
        habitsService.updateHabit(habit.toDto())
        habitDao.update(habit.toEntity())
    }
}