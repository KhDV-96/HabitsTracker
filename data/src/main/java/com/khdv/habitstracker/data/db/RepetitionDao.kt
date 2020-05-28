package com.khdv.habitstracker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class RepetitionDao {

    @Transaction
    open suspend fun updateAll(habitId: String, repetitions: List<Repetition>) {
        deleteAll(habitId)
        insertAll(repetitions.map { it.copy(habitId = habitId) })
    }

    @Query("SELECT * FROM repetitions WHERE habitId = :habitId")
    abstract suspend fun getByHabitId(habitId: String): List<Repetition>

    @Insert
    abstract suspend fun insert(repetition: Repetition)

    @Insert
    abstract suspend fun insertAll(repetitions: List<Repetition>)

    @Query("DELETE FROM repetitions WHERE habitId = :habitId")
    abstract suspend fun deleteAll(habitId: String)
}