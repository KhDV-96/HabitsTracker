package com.khdv.habitstracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

const val DATABASE_FILE = "habits_tracker.db"

@Database(entities = [HabitEntity::class, Repetition::class], version = 1, exportSchema = false)
abstract class HabitsTrackerDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao

    abstract fun repetitionDao(): RepetitionDao
}