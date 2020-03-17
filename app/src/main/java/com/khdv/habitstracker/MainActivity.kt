package com.khdv.habitstracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.adapter.HabitsAdapter

class MainActivity : AppCompatActivity() {

    private val habits = mutableListOf<Habit>()
    private val adapter = HabitsAdapter(habits)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.habits)
        recyclerView.adapter = adapter
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
    }
}
