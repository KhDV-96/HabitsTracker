package com.khdv.habitstracker.presentation.screens.home

import androidx.lifecycle.*
import com.khdv.habitstracker.domain.interactors.LoadHabitsUseCase
import com.khdv.habitstracker.domain.interactors.RepeatHabitUseCase
import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.domain.shared.Result
import com.khdv.habitstracker.presentation.ActionEvent
import com.khdv.habitstracker.presentation.ContentEvent
import com.khdv.habitstracker.presentation.util.Order
import com.khdv.habitstracker.presentation.util.repeatUntilSuccess
import kotlinx.coroutines.launch

class HabitsViewModel(
    loadUseCase: LoadHabitsUseCase,
    private val repeatUseCase: RepeatHabitUseCase,
    private val requestDelay: Long
) : ViewModel() {

    private val requestResult = loadUseCase.loadHabits().asLiveData()
    private val habits = MediatorLiveData<List<Habit>>()
    private val displayedHabits = MediatorLiveData<Sequence<Habit>>()
    private val typedHabits = mutableMapOf<Habit.Type, LiveData<List<Habit>>>()
    private val priorityOrder = MutableLiveData<Order>()

    val titleFilter = MutableLiveData<String>()

    private val _showRepeatingStatus = MutableLiveData<ContentEvent<Pair<Habit, Int>>>()
    val showRepeatingStatus: MutableLiveData<ContentEvent<Pair<Habit, Int>>>
        get() = _showRepeatingStatus

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
                is Result.Error<*> -> {
                    _error.value = ContentEvent(it.throwable)
                    reloadHabits(it)
                }
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
        _navigateToHabitEditing.value = ContentEvent(habit.id)
    }

    fun repeatHabit(habit: Habit) {
        viewModelScope.launch {
            when (val result = repeatUseCase.repeatHabit(habit)) {
                is Result.Success -> _showRepeatingStatus.value = ContentEvent(habit to result.data)
                is Result.Error<*> -> _error.value = ContentEvent(result.throwable)
            }
        }
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

    private fun reloadHabits(result: Result<List<Habit>>) = viewModelScope.launch {
        result.repeatUntilSuccess(requestDelay)
    }
}