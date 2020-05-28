package com.khdv.habitstracker.domain.repositories

import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.domain.shared.Result
import java.util.*

interface RepetitionsRepository {

    suspend fun getHabitRepetitions(habit: Habit): List<Date>

    suspend fun insert(habit: Habit, date: Date): Result<Unit>
}