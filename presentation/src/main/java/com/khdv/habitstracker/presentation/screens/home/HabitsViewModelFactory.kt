package com.khdv.habitstracker.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khdv.habitstracker.data.repositories.HabitsRepository

class HabitsViewModelFactory(
    private val repository: HabitsRepository,
    private val requestDelay: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitsViewModel(repository, requestDelay) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}