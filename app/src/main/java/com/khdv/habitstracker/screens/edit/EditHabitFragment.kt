package com.khdv.habitstracker.screens.edit

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.khdv.habitstracker.R
import com.khdv.habitstracker.data.HabitsRepository
import com.khdv.habitstracker.model.Habit
import kotlinx.android.synthetic.main.fragment_edit_habit.*

class EditHabitFragment : Fragment() {

    companion object {
        private const val CREATE_HABIT_ID = -1

        private fun getHabitType(radioButtonId: Int) = when (radioButtonId) {
            R.id.useful -> Habit.Type.USEFUL
            R.id.harmful -> Habit.Type.HARMFUL
            else -> throw IllegalStateException("Unknown habit type button")
        }

        private fun randomColor(): Int {
            val range = 0..255
            return Color.argb(255, range.random(), range.random(), range.random())
        }
    }

    private val viewModel: EditHabitViewModel by viewModels {
        val args = EditHabitFragmentArgs.fromBundle(arguments!!)
        val habitIndex = if (args.habitId == CREATE_HABIT_ID) null else args.habitId
        EditHabitViewModelFactory(HabitsRepository(), habitIndex)
    }
    private lateinit var requiredTextFields: List<EditText>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_habit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.habit.observe(viewLifecycleOwner, Observer {
            if (it == null) setDefaults() else fillFields(it)
        })
        viewModel.returnToHomeScreen.observe(viewLifecycleOwner, Observer {
            it.executeIfNotHandled { findNavController().navigateUp() }
        })

        requiredTextFields = listOf(title_view, quantity, periodicity)

        save_button.setOnClickListener { saveHabit() }
    }

    private fun fillFields(habit: Habit) {
        title_view.setText(habit.title)
        description.setText(habit.description)
        when (habit.type) {
            Habit.Type.USEFUL -> useful.isChecked = true
            Habit.Type.HARMFUL -> harmful.isChecked = true
        }
        priority.setSelection(habit.priority.ordinal)
        quantity.setText(habit.quantity.toString())
        periodicity.setText(habit.periodicity.toString())
    }

    private fun setDefaults() {
        type.check(R.id.useful)
        priority.setSelection(Habit.Priority.HIGH.ordinal)
    }

    private fun saveHabit() {
        if (verifyFields())
            viewModel.saveHabit(createHabit())
    }

    private fun verifyFields(): Boolean {
        requiredTextFields.firstOrNull { it.text.isEmpty() }?.apply {
            error = getString(R.string.required_error)
            return false
        }
        return true
    }

    private fun createHabit() = Habit(
        viewModel.habit.value?.id ?: CREATE_HABIT_ID,
        title_view.text.toString(),
        description.text.toString(),
        Habit.Priority.valueOf(priority.selectedItemPosition),
        getHabitType(type.checkedRadioButtonId),
        quantity.text.toString().toInt(),
        periodicity.text.toString().toInt(),
        viewModel.habit.value?.color ?: randomColor()
    )
}
