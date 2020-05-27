package com.khdv.habitstracker.presentation.screens.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khdv.habitstracker.domain.interactors.GetHabitUseCase
import com.khdv.habitstracker.domain.interactors.ManageHabitUseCase

class EditHabitViewModelFactory(
    private val getHabitUseCase: GetHabitUseCase,
    private val manageHabitUseCase: ManageHabitUseCase,
    private val habitId: String?,
    private val requestDelay: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditHabitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditHabitViewModel(
                getHabitUseCase,
                manageHabitUseCase,
                habitId,
                requestDelay
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}