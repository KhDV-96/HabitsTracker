package com.khdv.habitstracker.domain.interactors

import com.khdv.habitstracker.domain.models.Habit
import javax.inject.Inject

class ManageHabitUseCase @Inject constructor(private val repository: HabitsRepository) {

    suspend fun saveHabit(habit: Habit) = if (habit.id.isEmpty()) {
        repository.insert(habit)
    } else {
        repository.update(habit)
    }

    suspend fun deleteHabit(habit: Habit) = repository.delete(habit)
}