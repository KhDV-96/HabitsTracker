package com.khdv.habitstracker.network

class HabitsApiException(val code: Int, message: String) : Exception(message) {
}