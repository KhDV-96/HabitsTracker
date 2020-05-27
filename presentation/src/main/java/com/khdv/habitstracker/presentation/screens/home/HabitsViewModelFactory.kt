package com.khdv.habitstracker.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khdv.habitstracker.domain.interactors.LoadHabitsUseCase

class HabitsViewModelFactory(
    private val useCase: LoadHabitsUseCase,
    private val requestDelay: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitsViewModel(useCase, requestDelay) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}