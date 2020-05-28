package com.khdv.habitstracker.domain.services

import com.khdv.habitstracker.domain.models.Habit
import java.util.*
import kotlin.math.max

object HabitAnalyzer {

    fun countRemainingRepsForToday(habit: Habit, repetitions: List<Date>): Int {
        val today = getCurrentDate()
        if (getFinishDate(habit).before(today)) return 0
        val todayRepetitions = repetitions.filter { it.time >= today.timeInMillis }
        return max(habit.quantity - todayRepetitions.size, 0)
    }

    private fun getCurrentDate(): Calendar {
        val today = Calendar.getInstance()
        today[Calendar.HOUR_OF_DAY] = 0
        today[Calendar.MINUTE] = 0
        today[Calendar.SECOND] = 0
        today[Calendar.MILLISECOND] = 0
        return today
    }

    private fun getFinishDate(habit: Habit): Calendar {
        val finishDate = Calendar.getInstance()
        finishDate.time = habit.date
        finishDate.add(Calendar.DAY_OF_YEAR, habit.periodicity)
        return finishDate
    }
}