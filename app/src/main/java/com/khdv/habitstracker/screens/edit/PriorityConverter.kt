package com.khdv.habitstracker.screens.edit

import androidx.databinding.InverseMethod
import com.khdv.habitstracker.model.Habit

object PriorityConverter {

    @InverseMethod("intToPriority")
    @JvmStatic
    fun toInt(priority: Habit.Priority): Int {
        return priority.ordinal
    }

    @JvmStatic
    fun intToPriority(ordinal: Int): Habit.Priority {
        return Habit.Priority.valueOf(ordinal)
    }
}