package com.khdv.habitstracker.data.network

import com.squareup.moshi.Json

data class HabitDto(
    val color: Int,
    val count: Int,
    val date: Long,
    val description: String,
    @field:Json(name = "done_dates") val doneDates: List<Long>,
    val frequency: Int,
    val priority: Int,
    val title: String,
    val type: Int,
    val uid: String
)

data class HabitUidDto(
    val uid: String
)

data class ErrorDto(
    val code: Int,
    val message: String
)