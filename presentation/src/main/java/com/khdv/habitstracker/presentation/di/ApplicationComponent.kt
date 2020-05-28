package com.khdv.habitstracker.presentation.di

import com.khdv.habitstracker.data.di.HabitsModule
import com.khdv.habitstracker.presentation.screens.edit.EditHabitFragment
import com.khdv.habitstracker.presentation.screens.home.HabitFiltersAndSortsFragment
import com.khdv.habitstracker.presentation.screens.home.HabitListFragment
import com.khdv.habitstracker.presentation.screens.home.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, HabitsModule::class])
interface ApplicationComponent {
    fun inject(fragment: HomeFragment)
    fun inject(fragment: HabitListFragment)
    fun inject(fragment: HabitFiltersAndSortsFragment)
    fun inject(fragment: EditHabitFragment)
}