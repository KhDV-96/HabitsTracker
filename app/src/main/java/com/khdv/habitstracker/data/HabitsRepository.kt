package com.khdv.habitstracker.data

import androidx.lifecycle.map
import com.khdv.habitstracker.db.HabitDao
import com.khdv.habitstracker.db.HabitEntity
import com.khdv.habitstracker.model.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitsRepository(private val habitDao: HabitDao) {

    fun getAll() = habitDao.getAll().map { it.map(HabitEntity::toModel) }

    suspend fun getById(id: Int) = withContext(Dispatchers.IO) {
        habitDao.getById(id).toModel()
    }

    suspend fun insert(habit: Habit) = withContext(Dispatchers.IO) {
        habitDao.insert(habit.toEntity())
    }

    suspend fun update(habit: Habit) = withContext(Dispatchers.IO) {
        habitDao.update(habit.toEntity())
    }
}