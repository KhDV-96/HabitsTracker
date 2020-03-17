package com.khdv.habitstracker.model

data class Habit(
    val title: String,
    val description: String,
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