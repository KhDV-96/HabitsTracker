package com.khdv.habitstracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.khdv.habitstracker.adapter.HabitClickListener
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.adapter.HabitsAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CREATE_HABIT_REQUEST_CODE = 1
        private const val EDIT_HABIT_REQUEST_CODE = 2
        private const val HABIT_LIST_KEY = "HABIT_LIST"
    }

    private lateinit var habits: ArrayList<Habit>
    private lateinit var adapter: HabitsAdapter
    private var editableHabitIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        habits = savedInstanceState?.getParcelableArrayList(HABIT_LIST_KEY) ?: ArrayList()
        adapter = HabitsAdapter(habits, HabitClickListener(::navigateToEditHabit))

        habit_list.adapter = adapter
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        habit_list.addItemDecoration(decoration)

        add_habit_button.setOnClickListener { navigateToCreateHabit() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        val habit = data?.getParcelableExtra<Habit>(EditHabitActivity.HABIT_KEY) ?: return
        when (requestCode) {
            CREATE_HABIT_REQUEST_CODE -> {
                habits.add(habit)
                adapter.notifyItemInserted(habits.size - 1)
            }
            EDIT_HABIT_REQUEST_CODE -> {
                habits[editableHabitIndex] = habit
                adapter.notifyItemChanged(editableHabitIndex)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(HABIT_LIST_KEY, habits)
    }

    private fun navigateToCreateHabit() {
        Intent(this, EditHabitActivity::class.java).apply {
            startActivityForResult(this, CREATE_HABIT_REQUEST_CODE)
        }
    }

    private fun navigateToEditHabit(habit: Habit) {
        editableHabitIndex = habits.indexOf(habit)
        Intent(this, EditHabitActivity::class.java).apply {
            putExtra(EditHabitActivity.HABIT_KEY, habit)
            startActivityForResult(this, EDIT_HABIT_REQUEST_CODE)
        }
    }
}
