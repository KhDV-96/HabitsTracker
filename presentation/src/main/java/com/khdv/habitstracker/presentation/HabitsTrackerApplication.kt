package com.khdv.habitstracker.presentation

import android.app.Application
import com.khdv.habitstracker.data.di.HabitsModule
import com.khdv.habitstracker.presentation.di.ApplicationComponent
import com.khdv.habitstracker.presentation.di.DaggerApplicationComponent

class HabitsTrackerApplication : Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent
            .builder()
            .habitsModule(HabitsModule(this))
            .build()
    }
}