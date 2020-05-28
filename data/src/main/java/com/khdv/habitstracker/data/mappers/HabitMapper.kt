package com.khdv.habitstracker.data.mappers

import com.khdv.habitstracker.data.db.HabitEntity
import com.khdv.habitstracker.data.db.HabitWithRepetitions
import com.khdv.habitstracker.data.db.Repetition
import com.khdv.habitstracker.data.network.ErrorDto
import com.khdv.habitstracker.data.network.HabitDto
import com.khdv.habitstracker.data.network.HabitsApiException
import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.domain.shared.toEnum
import java.util.*

internal fun HabitWithRepetitions.toModel() = Habit(
    habit.id, habit.title, habit.description, habit.date, habit.priority,
    habit.type, habit.quantity, habit.periodicity, habit.color
)

internal fun Habit.toEntity() = HabitEntity(
    id, title, description, date, priority, type, quantity, periodicity, color
)

internal fun HabitDto.toEntity() = HabitWithRepetitions(
    HabitEntity(
        uid, title, description, Date(date), priority.toEnum(),
        type.toEnum(), count, frequency, color
    ),
    doneDates.map { Repetition(uid, Date(it)) }
)

internal fun Habit.toDto() = HabitDto(
    color, quantity, date.time, description, periodicity, priority.ordinal, title, type.ordinal, id
)

internal fun ErrorDto.toException() = HabitsApiException(code, message)