package com.khdv.habitstracker.data.mappers

import com.khdv.habitstracker.data.db.HabitEntity
import com.khdv.habitstracker.data.network.ErrorDto
import com.khdv.habitstracker.data.network.HabitDto
import com.khdv.habitstracker.data.network.HabitsApiException
import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.domain.shared.toEnum
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

internal fun ErrorDto.toException() = HabitsApiException(code, message)