package com.khdv.habitstracker.data

import androidx.lifecycle.map
import com.khdv.habitstracker.db.HabitDao
import com.khdv.habitstracker.db.HabitEntity
import com.khdv.habitstracker.model.Habit

class HabitsRepository(private val habitDao: HabitDao) {

    fun getAll() = habitDao.getAll().map { it.map(HabitEntity::toModel) }

    fun getById(id: Int) = habitDao.getById(id).toModel()

    fun insert(habit: Habit) = habitDao.insert(habit.toEntity())

    fun update(habit: Habit) = habitDao.update(habit.toEntity())
}