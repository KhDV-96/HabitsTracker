package com.khdv.habitstracker.screens.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khdv.habitstracker.model.Habit

class HabitsAdapter(
    private val habits: List<Habit>,
    private val clickListener: HabitClickListener
) : RecyclerView.Adapter<HabitViewHolder>() {

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
}

class HabitClickListener(private val callback: (Habit) -> Unit) {
    fun onClick(habit: Habit) = callback(habit)
}