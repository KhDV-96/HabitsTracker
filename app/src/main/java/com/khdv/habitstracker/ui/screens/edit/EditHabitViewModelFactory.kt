package com.khdv.habitstracker.ui.screens.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khdv.habitstracker.data.HabitsRepository

class EditHabitViewModelFactory(
    private val repository: HabitsRepository,
    private val habitId: String?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditHabitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditHabitViewModel(repository, habitId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}