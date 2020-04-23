package com.khdv.habitstracker.util

import kotlinx.coroutines.delay

internal inline fun <reified T : Enum<T>> Int.toEnum() = enumValues<T>()[this]

suspend fun <T> Result<T>.repeatUntilSuccess(
    timeMillis: Long,
    onError: ((Throwable) -> Unit)? = null
): T {
    var result = this
    while (true) {
        when (result) {
            is Result.Success -> return result.data
            is Result.Error<*> -> {
                onError?.invoke(result.throwable)
                delay(timeMillis)
                @Suppress("UNCHECKED_CAST")
                result = result.retry() as Result<T>
            }
        }
    }
}