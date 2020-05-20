package com.khdv.habitstracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits")
    fun getAll(): LiveData<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getById(id: String): HabitEntity

    @Insert
    suspend fun insert(habit: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(habits: List<HabitEntity>)

    @Update
    suspend fun update(habit: HabitEntity)

    @Delete
    suspend fun delete(habit: HabitEntity)
}