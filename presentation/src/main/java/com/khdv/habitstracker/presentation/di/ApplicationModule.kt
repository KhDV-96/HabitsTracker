package com.khdv.habitstracker.presentation.di

import com.khdv.habitstracker.domain.services.HabitAnalyzer
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    fun provideHabitAnalyzer() = HabitAnalyzer
}