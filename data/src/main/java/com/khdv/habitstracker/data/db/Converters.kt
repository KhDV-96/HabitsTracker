package com.khdv.habitstracker.data.db

import androidx.room.TypeConverter
import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.domain.shared.toEnum
import java.util.*

object HabitPriorityConverter {

    @TypeConverter
    @JvmStatic
    fun priorityToInt(priority: Habit.Priority) = priority.ordinal

    @TypeConverter
    @JvmStatic
    fun priorityFromInt(ordinal: Int) = ordinal.toEnum<Habit.Priority>()
}

object HabitTypeConverter {

    @TypeConverter
    @JvmStatic
    fun typeToInt(type: Habit.Type) = type.ordinal

    @TypeConverter
    @JvmStatic
    fun typeFromInt(ordinal: Int) = ordinal.toEnum<Habit.Type>()
}

object DateConverter {

    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: Date) = date.time

    @TypeConverter
    @JvmStatic
    fun timestampToDate(timestamp: Long) = Date(timestamp)
}