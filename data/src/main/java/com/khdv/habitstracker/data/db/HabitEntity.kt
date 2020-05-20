package com.khdv.habitstracker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.domain.shared.toEnum
import java.util.*

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    @field:TypeConverters(DateConverter::class) val date: Date,
    @field:TypeConverters(HabitPriorityConverter::class) val priority: Habit.Priority,
    @field:TypeConverters(HabitTypeConverter::class) val type: Habit.Type,
    val quantity: Int,
    val periodicity: Int,
    val color: Int
)

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