package com.khdv.habitstracker.domain.repositories

import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.domain.shared.Result
import kotlinx.coroutines.flow.Flow

interface HabitsRepository {

    fun getAll(): Flow<Result<List<Habit>>>

    suspend fun getById(id: String): Habit

    suspend fun insert(habit: Habit): Result<*>

    suspend fun update(habit: Habit): Result<*>

    suspend fun delete(habit: Habit): Result<*>
}