package com.khdv.habitstracker.presentation.screens.edit

import androidx.databinding.InverseMethod
import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.domain.shared.toEnum

object PriorityConverter {

    @InverseMethod("intToPriority")
    @JvmStatic
    fun toInt(priority: Habit.Priority) = priority.ordinal

    @JvmStatic
    fun intToPriority(ordinal: Int) = ordinal.toEnum<Habit.Priority>()
}