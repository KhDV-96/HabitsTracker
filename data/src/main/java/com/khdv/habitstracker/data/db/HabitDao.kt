package com.khdv.habitstracker.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class HabitDao(database: HabitsTrackerDatabase) {

    private val repetitionDao = database.repetitionDao()

    @Transaction
    open suspend fun updateAll(habits: List<HabitWithRepetitions>) {
        for (habit in habits) {
            insert(habit.habit)
            repetitionDao.updateAll(habit.habit.id, habit.repetitions)
        }
    }

    @Transaction
    @Query("SELECT * FROM habits")
    abstract fun getAll(): Flow<List<HabitWithRepetitions>>

    @Transaction
    @Query("SELECT * FROM habits WHERE id = :id")
    abstract suspend fun getById(id: String): HabitWithRepetitions

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(habit: HabitEntity)

    @Update
    abstract suspend fun update(habit: HabitEntity)

    @Delete
    abstract suspend fun delete(habit: HabitEntity)
}