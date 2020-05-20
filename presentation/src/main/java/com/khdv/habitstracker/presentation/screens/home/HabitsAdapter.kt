package com.khdv.habitstracker.presentation.screens.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khdv.habitstracker.domain.models.Habit

class HabitsAdapter(private val clickListener: HabitClickListener) :
    RecyclerView.Adapter<HabitViewHolder>() {

    private var habits = emptyList<Habit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder.create(
            parent,
            clickListener
        )
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount() = habits.size

    fun submitList(habits: List<Habit>) {
        this.habits = habits
        notifyDataSetChanged()
    }
}

class HabitClickListener(private val callback: (Habit) -> Unit) {
    fun onClick(habit: Habit) = callback(habit)
}