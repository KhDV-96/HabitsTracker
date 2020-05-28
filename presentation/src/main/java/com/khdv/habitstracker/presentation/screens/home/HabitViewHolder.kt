package com.khdv.habitstracker.presentation.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khdv.habitstracker.R
import com.khdv.habitstracker.databinding.ItemHabitBinding
import com.khdv.habitstracker.domain.models.Habit

class HabitViewHolder(private val binding: ItemHabitBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(
            parent: ViewGroup,
            clickListener: HabitClickListener,
            doneListener: HabitDoneListener
        ): HabitViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemHabitBinding.inflate(inflater, parent, false)
            binding.clickListener = clickListener
            binding.doneListener = doneListener
            return HabitViewHolder(binding)
        }

        private fun getPriorityLabelResource(priority: Habit.Priority) = when (priority) {
            Habit.Priority.LOW -> R.string.low_label
            Habit.Priority.MEDIUM -> R.string.medium_label
            Habit.Priority.HIGH -> R.string.high_label
        }
    }

    fun bind(habit: Habit) {
        binding.habit = habit
        binding.info.text = formatInfo(habit)
        binding.executePendingBindings()
    }

    private fun formatInfo(habit: Habit) = with(itemView.resources) {
        val priority = getString(getPriorityLabelResource(habit.priority))
        val times = getQuantityString(R.plurals.time_label, habit.quantity)
        val days = getQuantityString(R.plurals.day_label, habit.periodicity)
        getString(
            R.string.habit_info_format, priority, habit.quantity, times, habit.periodicity, days
        )
    }
}