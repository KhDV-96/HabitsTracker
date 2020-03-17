package com.khdv.habitstracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.adapter.HabitsAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val habits = mutableListOf<Habit>()
    private val adapter = HabitsAdapter(habits)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        habit_list.adapter = adapter
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        habit_list.addItemDecoration(decoration)

        val addHabitButton = findViewById<FloatingActionButton>(R.id.add_habit)
        addHabitButton.setOnClickListener {
            Intent(this, EditHabitActivity::class.java).apply(::startActivity)
        }
    }
}
