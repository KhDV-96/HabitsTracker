package com.khdv.habitstracker.screens.home

import androidx.lifecycle.*
import com.khdv.habitstracker.data.HabitsRepository
import com.khdv.habitstracker.data.Order
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.util.ActionEvent
import com.khdv.habitstracker.util.ContentEvent

class HabitsViewModel(private val repository: HabitsRepository) : ViewModel() {

    private val habits = MediatorLiveData<List<Habit>>()
    private val typedHabits = mutableMapOf<Habit.Type, LiveData<List<Habit>>>()
    private val priorityOrder = MutableLiveData(Order.ARBITRARY)

    val titleFilter = MutableLiveData("")

    private val _navigateToHabitCreation = MutableLiveData<ActionEvent>()
    val navigateToHabitCreation: LiveData<ActionEvent>
        get() = _navigateToHabitCreation

    private val _navigateToHabitEditing = MutableLiveData<ContentEvent<Int>>()
    val navigateToHabitEditing: LiveData<ContentEvent<Int>>
        get() = _navigateToHabitEditing

    init {
        habits.addSource(titleFilter) { loadHabits(it, priorityOrder.value) }
        habits.addSource(priorityOrder) { loadHabits(titleFilter.value, it) }
    }

    fun loadHabits() = loadHabits(titleFilter.value, priorityOrder.value)

    fun getHabitsWithType(type: Habit.Type) = typedHabits.getOrPut(type) {
        habits.map {
            it.filter { habit -> habit.type == type }
        }
    }

    fun setPriorityOrder(order: Order) {
        priorityOrder.value = when (order) {
            priorityOrder.value -> Order.ARBITRARY
            else -> order
        }
    }

    fun createHabit() {
        _navigateToHabitCreation.value = ActionEvent()
    }

    fun editHabit(habit: Habit) {
        _navigateToHabitEditing.value = ContentEvent(habit.id)
    }

    private fun loadHabits(title: String?, order: Order?) {
        habits.value = repository.find(title!!, order!!)
    }
}