package com.khdv.habitstracker.data.utils

import com.khdv.habitstracker.data.mappers.toException
import com.khdv.habitstracker.data.network.error
import com.khdv.habitstracker.domain.shared.Result
import retrofit2.HttpException

internal suspend fun <T, R> performSafely(
    retry: suspend () -> Result<T>,
    call: suspend () -> R
): Result<R> = try {
    Result.Success(call.invoke())
} catch (exception: HttpException) {
    val error = exception.response()?.error()
    Result.Error(error?.toException() ?: exception, retry)
} catch (exception: Exception) {
    Result.Error(exception, retry)
}