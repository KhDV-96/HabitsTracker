package com.khdv.habitstracker.presentation.screens.edit

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khdv.habitstracker.domain.interactors.GetHabitUseCase
import com.khdv.habitstracker.domain.interactors.ManageHabitUseCase
import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.domain.shared.Result
import com.khdv.habitstracker.presentation.ActionEvent
import com.khdv.habitstracker.presentation.ContentEvent
import com.khdv.habitstracker.presentation.util.repeatUntilSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class EditHabitViewModel(
    private val getHabitUseCase: GetHabitUseCase,
    private val manageHabitUseCase: ManageHabitUseCase,
    private val habitId: String?,
    private val requestDelay: Long
) : ViewModel() {

    companion object {
        private fun randomColor(): Int {
            val range = 0..255
            return Color.argb(255, range.random(), range.random(), range.random())
        }
    }

    private lateinit var habit: Habit
    private lateinit var lastError: Throwable
    private var job: Job? = null
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
        if (job?.isActive == true) {
            showError(lastError)
            return
        }
        val habit = createHabit()
        job = viewModelScope.launch {
            handleResult(manageHabitUseCase.saveHabit(habit))
        }
    }

    fun deleteHabit() {
        if (job?.isActive == true) {
            showError(lastError)
            return
        }
        job = viewModelScope.launch {
            handleResult(manageHabitUseCase.deleteHabit(habit))
        }
    }

    private fun loadHabit(id: String) = viewModelScope.launch {
        habit = getHabitUseCase.getHabit(id)
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

    private suspend fun <T> handleResult(result: Result<T>) {
        when (result) {
            is Result.Success -> _returnToHomeScreen.value =
                ActionEvent()
            is Result.Error<*> -> {
                showError(result.throwable)
                result.repeatUntilSuccess<Unit>(requestDelay) { lastError = it }
                _returnToHomeScreen.value =
                    ActionEvent()
            }
        }
    }

    private fun showError(throwable: Throwable) {
        _error.value =
            ContentEvent(throwable)
    }
}