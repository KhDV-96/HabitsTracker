package com.khdv.habitstracker.data.network

class HabitsApiException(val code: Int, message: String) : Exception(message) {
}