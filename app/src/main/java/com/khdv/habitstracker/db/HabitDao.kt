package com.khdv.habitstracker.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits")
    fun getAll(): LiveData<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun getById(id: Int): HabitEntity

    @Insert
    fun insert(habit: HabitEntity)

    @Update
    fun update(habit: HabitEntity)
}