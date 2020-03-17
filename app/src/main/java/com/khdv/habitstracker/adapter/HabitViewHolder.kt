package com.khdv.habitstracker.adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.khdv.habitstracker.R
import com.khdv.habitstracker.model.Habit

class HabitViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): HabitViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_habit, parent, false)
            return HabitViewHolder(view)
        }

        private fun getPriorityLabelResource(priority: Habit.Priority) = when (priority) {
            Habit.Priority.LOW -> R.string.low_label
            Habit.Priority.MEDIUM -> R.string.medium_label
            Habit.Priority.HIGH -> R.string.high_label
        }

        private fun getTypeImageResource(type: Habit.Type) = when (type) {
            Habit.Type.USEFUL -> R.drawable.ic_thumb_up_white_24dp
            Habit.Type.HARMFUL -> R.drawable.ic_thumb_down_white_24dp
        }
    }

    private val title = itemView.findViewById<TextView>(R.id.title)
    private val description = itemView.findViewById<TextView>(R.id.description)
    private val info = itemView.findViewById<TextView>(R.id.info)
    private val typeImage = itemView.findViewById<ImageView>(R.id.type)

    fun bind(habit: Habit) {
        title.text = habit.title
        description.text = habit.description
        info.text = formatInfo(habit)
        typeImage.apply {
            setImageResource(getTypeImageResource(habit.type))
            setColorFilter(habit.color, PorterDuff.Mode.SRC_IN)
        }
    }

    private fun formatInfo(habit: Habit) = with(itemView.resources) {
        val priority = getString(getPriorityLabelResource(habit.priority))
        getString(R.string.habit_info_format, priority, habit.quantity)
    }
}