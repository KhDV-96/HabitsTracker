package com.khdv.habitstracker.domain.shared

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val throwable: Throwable, val retry: suspend () -> Result<T>) :
        Result<Nothing>()
}