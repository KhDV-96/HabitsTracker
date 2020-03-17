package com.khdv.habitstracker.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khdv.habitstracker.model.Habit

class HabitsAdapter(private val habits: List<Habit>) : RecyclerView.Adapter<HabitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount() = habits.size
}