package com.khdv.habitstracker.data

import com.khdv.habitstracker.model.Habit

class HabitsRepository {

    companion object {
        private val habits = mutableListOf<Habit>()
    }

    fun getAllHabits(): List<Habit> = habits

    fun findById(id: Int) = habits.getOrNull(id)

    fun insert(habit: Habit) {
        habits.add(habit.copy(id = habits.size))
    }

    fun update(habit: Habit) {
        habits[habit.id] = habit
    }
}