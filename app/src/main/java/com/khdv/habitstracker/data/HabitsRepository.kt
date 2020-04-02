package com.khdv.habitstracker.data

import com.khdv.habitstracker.model.Habit

class HabitsRepository {

    companion object {
        private val habits = mutableListOf<Habit>()
    }

    fun find(title: String = "", priorityOrder: Order = Order.ARBITRARY) = habits.asSequence()
        .filter { it.title.contains(title, true) }
        .sortedWith(Comparator { h1, h2 ->
            priorityOrder.value * h1.priority.compareTo(h2.priority)
        })
        .toList()

    fun findById(id: Int) = habits.getOrNull(id)

    fun insert(habit: Habit) {
        habits.add(habit.copy(id = habits.size))
    }

    fun update(habit: Habit) {
        habits[habit.id] = habit
    }
}