package com.khdv.habitstracker.data.db

import androidx.room.*
import com.khdv.habitstracker.domain.models.Habit
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

@Entity(tableName = "repetitions", primaryKeys = ["habitId", "date"])
data class Repetition(
    @ForeignKey(
        entity = HabitEntity::class,
        parentColumns = ["id"],
        childColumns = ["habitId"],
        onDelete = ForeignKey.CASCADE
    )
    val habitId: String,
    @field:TypeConverters(DateConverter::class) val date: Date
)

data class HabitWithRepetitions(
    @Embedded val habit: HabitEntity,
    @Relation(parentColumn = "id", entityColumn = "habitId")
    val repetitions: List<Repetition>
)