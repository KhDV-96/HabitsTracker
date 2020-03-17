package com.khdv.habitstracker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Habit(
    val title: String,
    val description: String,
    val priority: Priority,
    val type: Type,
    val quantity: Int,
    val periodicity: Int,
    val color: Int
) : Parcelable {
    enum class Priority {
        LOW, MEDIUM, HIGH;

        companion object {
            fun valueOf(ordinal: Int) = values().first { it.ordinal == ordinal }
        }
    }

    enum class Type {
        USEFUL, HARMFUL
    }
}