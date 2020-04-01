package com.khdv.habitstracker.screens.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khdv.habitstracker.data.HabitsRepository
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.util.ActionEvent

class EditHabitViewModel(private val repository: HabitsRepository, private val habitId: Int?) :
    ViewModel() {

    private val _habit = MutableLiveData<Habit>()
    val habit: LiveData<Habit>
        get() = _habit

    private val _returnToHomeScreen = MutableLiveData<ActionEvent>()
    val returnToHomeScreen: LiveData<ActionEvent>
        get() = _returnToHomeScreen

    init {
        _habit.value = when (habitId) {
            null -> null
            else -> repository.findById(habitId)
        }
    }

    fun saveHabit(habit: Habit) {
        when (habitId) {
            null -> repository.insert(habit)
            else -> repository.update(habit)
        }
        _returnToHomeScreen.value = ActionEvent()
    }
}