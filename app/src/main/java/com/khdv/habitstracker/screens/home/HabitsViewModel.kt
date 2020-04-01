package com.khdv.habitstracker.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.khdv.habitstracker.data.HabitsRepository
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.util.ActionEvent
import com.khdv.habitstracker.util.ContentEvent

class HabitsViewModel(repository: HabitsRepository) : ViewModel() {

    private val habits = MutableLiveData<List<Habit>>()
    private val typedHabits = mapOf<Habit.Type, LiveData<List<Habit>>>()

    private val _navigateToHabitCreation = MutableLiveData<ActionEvent>()
    val navigateToHabitCreation: LiveData<ActionEvent>
        get() = _navigateToHabitCreation

    private val _navigateToHabitEditing = MutableLiveData<ContentEvent<Int>>()
    val navigateToHabitEditing: LiveData<ContentEvent<Int>>
        get() = _navigateToHabitEditing

    init {
        habits.value = repository.getAllHabits()
    }

    fun getHabitsWithType(type: Habit.Type) = typedHabits.getOrElse(type) {
        habits.map {
            it.filter { habit -> habit.type == type }
        }
    }

    fun createHabit() {
        _navigateToHabitCreation.value = ActionEvent()
    }

    fun editHabit(habit: Habit) {
        _navigateToHabitEditing.value = ContentEvent(habit.id)
    }
}