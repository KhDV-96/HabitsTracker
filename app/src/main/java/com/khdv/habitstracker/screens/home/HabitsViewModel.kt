package com.khdv.habitstracker.screens.home

import androidx.lifecycle.*
import com.khdv.habitstracker.data.HabitsRepository
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.util.ActionEvent
import com.khdv.habitstracker.util.ContentEvent
import com.khdv.habitstracker.util.Order

class HabitsViewModel(repository: HabitsRepository) : ViewModel() {

    private val habits = repository.getAll()
    private val displayedHabits = MediatorLiveData<Sequence<Habit>>()
    private val typedHabits = mutableMapOf<Habit.Type, LiveData<List<Habit>>>()
    private val priorityOrder = MutableLiveData<Order>()

    val titleFilter = MutableLiveData<String>()

    private val _navigateToHabitCreation = MutableLiveData<ActionEvent>()
    val navigateToHabitCreation: LiveData<ActionEvent>
        get() = _navigateToHabitCreation

    private val _navigateToHabitEditing = MutableLiveData<ContentEvent<Int>>()
    val navigateToHabitEditing: LiveData<ContentEvent<Int>>
        get() = _navigateToHabitEditing

    init {
        displayedHabits.addSource(habits) { updateDisplayedHabits(it) }
        displayedHabits.addSource(titleFilter) { habits.value?.let(this::updateDisplayedHabits) }
        displayedHabits.addSource(priorityOrder) { habits.value?.let(this::updateDisplayedHabits) }
    }

    fun getHabitsWithType(type: Habit.Type) = typedHabits.getOrPut(type) {
        displayedHabits.map {
            it.filter { habit -> habit.type == type }.toList()
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

    private fun updateDisplayedHabits(habits: List<Habit>) {
        displayedHabits.value = habits.asSequence()
            .filterByTitle(titleFilter.value)
            .sortByPriority(priorityOrder.value)
    }

    private fun Sequence<Habit>.filterByTitle(title: String?) = when (title) {
        null -> this
        else -> filter { it.title.contains(title.trim(), true) }
    }

    private fun Sequence<Habit>.sortByPriority(order: Order?) = when (order) {
        null, Order.ARBITRARY -> this
        else -> sortedWith(Comparator { h1, h2 ->
            order.factor * h1.priority.compareTo(h2.priority)
        })
    }
}