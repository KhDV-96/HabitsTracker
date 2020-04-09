package com.khdv.habitstracker.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.util.toEnum

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
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