package com.khdv.habitstracker.ui.screens.home

import androidx.lifecycle.*
import com.khdv.habitstracker.data.HabitsRepository
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.ui.ActionEvent
import com.khdv.habitstracker.ui.ContentEvent
import com.khdv.habitstracker.util.Order
import com.khdv.habitstracker.util.Result

class HabitsViewModel(repository: HabitsRepository) : ViewModel() {

    private val requestResult = repository.getAll()
    private val habits = MediatorLiveData<List<Habit>>()
    private val displayedHabits = MediatorLiveData<Sequence<Habit>>()
    private val typedHabits = mutableMapOf<Habit.Type, LiveData<List<Habit>>>()
    private val priorityOrder = MutableLiveData<Order>()

    val titleFilter = MutableLiveData<String>()

    private val _error = MutableLiveData<ContentEvent<Throwable>>()
    val error: LiveData<ContentEvent<Throwable>>
        get() = _error

    private val _navigateToHabitCreation = MutableLiveData<ActionEvent>()
    val navigateToHabitCreation: LiveData<ActionEvent>
        get() = _navigateToHabitCreation

    private val _navigateToHabitEditing = MutableLiveData<ContentEvent<String>>()
    val navigateToHabitEditing: LiveData<ContentEvent<String>>
        get() = _navigateToHabitEditing

    init {
        habits.addSource(requestResult) {
            when (it) {
                is Result.Success -> habits.value = it.data
                is Result.Error -> _error.value =
                    ContentEvent(it.throwable)
            }
        }
    }

    init {
        displayedHabits.addSource(habits, this::updateDisplayedHabits)
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
        _navigateToHabitEditing.value =
            ContentEvent(habit.id)
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