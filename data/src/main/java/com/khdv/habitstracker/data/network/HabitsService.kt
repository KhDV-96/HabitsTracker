package com.khdv.habitstracker.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PUT

private const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
private const val API_TOKEN = "30b3b280-9072-424a-a89f-df6c957a5829"

interface HabitsService {

    @GET("habit")
    suspend fun getHabits(): List<HabitDto>

    @PUT("habit")
    suspend fun updateHabit(@Body habit: HabitDto): HabitUidDto

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(@Body habitUid: HabitUidDto)
}

object AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.header("Authorization", API_TOKEN)
        return chain.proceed(builder.build())
    }
}

private val client = OkHttpClient.Builder()
    .addInterceptor(AuthorizationInterceptor)
    .build()

val retrofit: Retrofit = Retrofit.Builder()
    .client(client)
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

private val errorConverter = retrofit
    .responseBodyConverter<ErrorDto>(ErrorDto::class.java, emptyArray())

fun <T> Response<T>.error() = errorBody()?.let(errorConverter::convert)