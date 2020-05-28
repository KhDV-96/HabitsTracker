package com.khdv.habitstracker.domain.interactors

import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.domain.repositories.RepetitionsRepository
import com.khdv.habitstracker.domain.services.HabitAnalyzer
import com.khdv.habitstracker.domain.shared.Result
import java.util.*
import javax.inject.Inject

class RepeatHabitUseCase @Inject constructor(
    private val repository: RepetitionsRepository,
    private val analyzer: HabitAnalyzer
) {

    suspend fun repeatHabit(habit: Habit) = when (val result = repository.insert(habit, Date())) {
        is Result.Success -> {
            val repetitions = repository.getHabitRepetitions(habit)
            Result.Success(analyzer.countRemainingRepsForToday(habit, repetitions))
        }
        else -> result as Result.Error<*>
    }
}