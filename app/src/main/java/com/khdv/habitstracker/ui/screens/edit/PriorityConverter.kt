package com.khdv.habitstracker.ui.screens.edit

import androidx.databinding.InverseMethod
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.util.toEnum

object PriorityConverter {

    @InverseMethod("intToPriority")
    @JvmStatic
    fun toInt(priority: Habit.Priority) = priority.ordinal

    @JvmStatic
    fun intToPriority(ordinal: Int) = ordinal.toEnum<Habit.Priority>()
}