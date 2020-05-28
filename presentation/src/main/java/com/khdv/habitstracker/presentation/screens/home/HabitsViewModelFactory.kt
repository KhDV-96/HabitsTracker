package com.khdv.habitstracker.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khdv.habitstracker.domain.interactors.LoadHabitsUseCase
import com.khdv.habitstracker.domain.interactors.RepeatHabitUseCase

class HabitsViewModelFactory(
    private val loadUseCase: LoadHabitsUseCase,
    private val repeatUseCase: RepeatHabitUseCase,
    private val requestDelay: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitsViewModel(loadUseCase, repeatUseCase, requestDelay) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}