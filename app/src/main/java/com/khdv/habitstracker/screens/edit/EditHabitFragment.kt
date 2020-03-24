package com.khdv.habitstracker.screens.edit

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.khdv.habitstracker.MainActivity
import com.khdv.habitstracker.R
import com.khdv.habitstracker.model.Habit
import kotlinx.android.synthetic.main.fragment_edit_habit.*

class EditHabitFragment : Fragment() {

    companion object {
        private fun getHabitType(radioButtonId: Int) = when (radioButtonId) {
            R.id.useful -> Habit.Type.USEFUL
            R.id.harmful -> Habit.Type.HARMFUL
            else -> throw IllegalStateException("Unknown habit type button")
        }

        private fun randomColor(): Int {
            val range = (0..255)
            return Color.argb(255, range.random(), range.random(), range.random())
        }
    }

    private lateinit var habits: MutableList<Habit>
    private lateinit var requiredTextFields: List<EditText>
    private var habitIndex: Int = -1
    private var color: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        habits = (activity as MainActivity).habits
        return inflater.inflate(R.layout.fragment_edit_habit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readArguments()

        requiredTextFields = listOf(title_view, quantity, periodicity)

        priority.setSelection(Habit.Priority.HIGH.ordinal)
        save_button.setOnClickListener { saveHabit() }
    }

    private fun readArguments() {
        val args = EditHabitFragmentArgs.fromBundle(arguments!!)
        habitIndex = args.habitIndex
        if (habitIndex > -1)
            fillFields(habits[habitIndex])
        else
            habitIndex = habits.size
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
        color = habit.color
    }

    private fun saveHabit() {
        if (verifyFields()) {
            if (habitIndex == habits.size)
                habits.add(createHabit())
            else
                habits[habitIndex] = createHabit()
            findNavController().navigateUp()
        }
    }

    private fun verifyFields(): Boolean {
        requiredTextFields.firstOrNull { it.text.isEmpty() }?.apply {
            error = getString(R.string.required_error)
            return false
        }
        return true
    }

    private fun createHabit() = Habit(
        title_view.text.toString(),
        description.text.toString(),
        Habit.Priority.valueOf(priority.selectedItemPosition),
        getHabitType(type.checkedRadioButtonId),
        quantity.text.toString().toInt(),
        periodicity.text.toString().toInt(),
        color ?: randomColor()
    )
}
