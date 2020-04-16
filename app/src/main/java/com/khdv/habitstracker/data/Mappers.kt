package com.khdv.habitstracker.data

import com.khdv.habitstracker.db.HabitEntity
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.network.HabitDto
import com.khdv.habitstracker.util.toEnum
import java.util.*

internal fun HabitEntity.toModel() = Habit(
    id, title, description, date, priority, type, quantity, periodicity, color
)

internal fun Habit.toEntity() = HabitEntity(
    id, title, description, date, priority, type, quantity, periodicity, color
)

internal fun HabitDto.toEntity() = HabitEntity(
    uid, title, description, Date(date), priority.toEnum(), type.toEnum(), count, frequency, color
)

internal fun Habit.toDto() = HabitDto(
    color, quantity, date.time, description, periodicity, priority.ordinal, title, type.ordinal, id
)