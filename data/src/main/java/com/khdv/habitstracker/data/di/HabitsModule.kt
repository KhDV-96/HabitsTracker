package com.khdv.habitstracker.data.di

import android.content.Context
import androidx.room.Room
import com.khdv.habitstracker.data.db.DATABASE_FILE
import com.khdv.habitstracker.data.db.HabitsTrackerDatabase
import com.khdv.habitstracker.data.network.HabitsService
import com.khdv.habitstracker.data.network.retrofit
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class HabitsModule(private val context: Context) {

    @Provides
    fun provideHabitDao(database: HabitsTrackerDatabase) = database.habitDao()

    @Provides
    fun provideDatabase(): HabitsTrackerDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            HabitsTrackerDatabase::class.java,
            DATABASE_FILE
        ).build()
    }

    @Provides
    fun provideHabitsService(retrofit: Retrofit): HabitsService {
        return retrofit.create(HabitsService::class.java)
    }

    @Provides
    fun provideRetrofit() = retrofit
}