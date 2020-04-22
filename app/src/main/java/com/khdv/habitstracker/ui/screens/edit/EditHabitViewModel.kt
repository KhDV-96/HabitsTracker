package com.khdv.habitstracker.ui.screens.edit

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khdv.habitstracker.data.HabitsRepository
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.ui.ActionEvent
import com.khdv.habitstracker.ui.ContentEvent
import com.khdv.habitstracker.util.Result
import kotlinx.coroutines.launch
import java.util.*

class EditHabitViewModel(private val repository: HabitsRepository, private val habitId: String?) :
    ViewModel() {

    companion object {
        private fun randomColor(): Int {
            val range = 0..255
            return Color.argb(255, range.random(), range.random(), range.random())
        }
    }

    private lateinit var habit: Habit
    val title = MutableLiveData<String>()
    val description = MutableLiveData("")
    val priority = MutableLiveData(Habit.Priority.HIGH)
    val type = MutableLiveData(Habit.Type.USEFUL)
    val quantity = MutableLiveData<String>()
    val periodicity = MutableLiveData<String>()
    private val color = MutableLiveData<Int>()

    private val _error = MutableLiveData<ContentEvent<Throwable>>()
    val error: LiveData<ContentEvent<Throwable>>
        get() = _error

    private val _returnToHomeScreen = MutableLiveData<ActionEvent>()
    val returnToHomeScreen: LiveData<ActionEvent>
        get() = _returnToHomeScreen

    init {
        habitId?.run(this::loadHabit)
    }

    fun setType(value: Habit.Type) {
        type.value = value
    }

    fun saveHabit() {
        val habit = createHabit()
        viewModelScope.launch {
            val result = when (habitId) {
                null -> repository.insert(habit)
                else -> repository.update(habit)
            }
            handleResult(result)
        }
    }

    fun deleteHabit() = viewModelScope.launch {
        val result = repository.delete(habit)
        handleResult(result)
    }

    private fun loadHabit(id: String) = viewModelScope.launch {
        habit = repository.getById(id)
        fillProperties(habit)
    }

    private fun fillProperties(habit: Habit) {
        title.value = habit.title
        description.value = habit.description
        priority.value = habit.priority
        type.value = habit.type
        quantity.value = habit.quantity.toString()
        periodicity.value = habit.periodicity.toString()
        color.value = habit.color
    }

    private fun createHabit() = Habit(
        habitId ?: "",
        title.value!!,
        description.value!!,
        Date(),
        priority.value!!,
        type.value!!,
        quantity.value!!.toInt(),
        periodicity.value!!.toInt(),
        color.value ?: randomColor()
    )

    private fun <T> handleResult(result: Result<T>) = when (result) {
        is Result.Success -> _returnToHomeScreen.value = ActionEvent()
        is Result.Error -> _error.value = ContentEvent(result.throwable)
    }
}