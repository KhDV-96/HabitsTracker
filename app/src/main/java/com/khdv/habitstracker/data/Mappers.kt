package com.khdv.habitstracker.data

import com.khdv.habitstracker.db.HabitEntity
import com.khdv.habitstracker.model.Habit

internal fun HabitEntity.toModel() = Habit(
    id, title, description, priority, type, quantity, periodicity, color
)

internal fun Habit.toEntity() = HabitEntity(
    id, title, description, priority, type, quantity, periodicity, color
)