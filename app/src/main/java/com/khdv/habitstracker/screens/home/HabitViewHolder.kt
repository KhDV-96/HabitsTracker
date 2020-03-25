package com.khdv.habitstracker.screens.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.khdv.habitstracker.R
import com.khdv.habitstracker.model.Habit

class HabitViewHolder(view: View, private val clickListener: HabitClickListener) :
    RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup, clickListener: HabitClickListener): HabitViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_habit, parent, false)
            return HabitViewHolder(view, clickListener)
        }

        private fun getPriorityLabelResource(priority: Habit.Priority) = when (priority) {
            Habit.Priority.LOW -> R.string.low_label
            Habit.Priority.MEDIUM -> R.string.medium_label
            Habit.Priority.HIGH -> R.string.high_label
        }
    }

    private val title = itemView.findViewById<TextView>(R.id.title_view)
    private val description = itemView.findViewById<TextView>(R.id.description)
    private val info = itemView.findViewById<TextView>(R.id.info)
    private val color = itemView.findViewById<View>(R.id.color)

    fun bind(habit: Habit) {
        title.text = habit.title
        description.text = habit.description
        info.text = formatInfo(habit)
        color.setBackgroundColor(habit.color)
        itemView.setOnClickListener { clickListener.onClick(habit) }
    }

    private fun formatInfo(habit: Habit) = with(itemView.resources) {
        val priority = getString(getPriorityLabelResource(habit.priority))
        getString(R.string.habit_info_format, priority, habit.quantity, habit.periodicity)
    }
}