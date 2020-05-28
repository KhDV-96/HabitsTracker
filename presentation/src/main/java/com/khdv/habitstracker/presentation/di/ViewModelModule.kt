package com.khdv.habitstracker.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khdv.habitstracker.presentation.screens.edit.EditHabitViewModel
import com.khdv.habitstracker.presentation.screens.home.HabitsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HabitsViewModel::class)
    abstract fun bindHabitsViewModel(viewModel: HabitsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditHabitViewModel::class)
    abstract fun bindEditHabitViewModel(viewModel: EditHabitViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}