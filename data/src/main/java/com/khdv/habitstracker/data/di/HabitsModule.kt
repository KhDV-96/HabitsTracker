package com.khdv.habitstracker.data.di

import android.content.Context
import androidx.room.Room
import com.khdv.habitstracker.data.db.DATABASE_FILE
import com.khdv.habitstracker.data.db.HabitsTrackerDatabase
import com.khdv.habitstracker.data.network.HabitsService
import com.khdv.habitstracker.data.network.retrofit
import com.khdv.habitstracker.data.repositories.HabitsRepositoryImpl
import com.khdv.habitstracker.data.repositories.RepetitionsRepositoryImpl
import com.khdv.habitstracker.domain.repositories.HabitsRepository
import com.khdv.habitstracker.domain.repositories.RepetitionsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [HabitsModule.Declarations::class])
class HabitsModule(private val context: Context) {

    @Provides
    fun provideHabitDao(database: HabitsTrackerDatabase) = database.habitDao()

    @Provides
    fun provideRepetitionsDao(database: HabitsTrackerDatabase) = database.repetitionDao()

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

    @Module
    interface Declarations {

        @Binds
        fun bindHabitsRepository(repository: HabitsRepositoryImpl): HabitsRepository

        @Binds
        fun bindRepetitionsRepository(repository: RepetitionsRepositoryImpl): RepetitionsRepository
    }
}