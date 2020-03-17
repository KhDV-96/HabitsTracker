package com.khdv.habitstracker

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.khdv.habitstracker.model.Habit
import kotlinx.android.synthetic.main.activity_edit_habit.*

class EditHabitActivity : AppCompatActivity() {

    companion object {
        const val HABIT_KEY = "HABIT"

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

    private lateinit var requiredTextFields: List<EditText>
    private var color: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_habit)

        requiredTextFields = listOf(title_view, quantity, periodicity)

        priority.setSelection(Habit.Priority.HIGH.ordinal)

        intent.getParcelableExtra<Habit>(HABIT_KEY)?.let(::fillFields)

        save_button.setOnClickListener {
            if (!verifyFields()) return@setOnClickListener
            val data = Intent().apply {
                putExtra(HABIT_KEY, createHabit())
            }
            setResult(Activity.RESULT_OK, data)
            finish()
        }
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
