package com.khdv.habitstracker.domain.models

import java.util.*

data class Habit(
    val id: String,
    val title: String,
    val description: String,
    val date: Date,
    val priority: Priority,
    val type: Type,
    val quantity: Int,
    val periodicity: Int,
    val color: Int
) {
    enum class Priority {
        LOW, MEDIUM, HIGH
    }

    enum class Type {
        USEFUL, HARMFUL
    }
}