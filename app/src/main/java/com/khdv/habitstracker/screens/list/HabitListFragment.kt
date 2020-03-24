package com.khdv.habitstracker.screens.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.khdv.habitstracker.MainActivity
import com.khdv.habitstracker.R
import com.khdv.habitstracker.model.Habit
import kotlinx.android.synthetic.main.fragment_habit_list.*

class HabitListFragment : Fragment() {

    private lateinit var habits: List<Habit>
    private lateinit var adapter: HabitsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        habits = (activity as MainActivity).habits
        adapter = HabitsAdapter(habits, HabitClickListener(::navigateToEditHabit))
        return inflater.inflate(R.layout.fragment_habit_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habit_list.adapter = adapter
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        habit_list.addItemDecoration(decoration)

        add_habit_button.setOnClickListener { navigateToCreateHabit() }
    }

    private fun navigateToCreateHabit() {
        val action = HabitListFragmentDirections.actionHabitListFragmentToEditHabitFragment()
        findNavController().navigate(action)
        adapter.notifyItemChanged(habits.size - 1)
    }

    private fun navigateToEditHabit(habit: Habit) {
        val index = habits.indexOf(habit)
        val action = HabitListFragmentDirections.actionHabitListFragmentToEditHabitFragment(index)
        findNavController().navigate(action)
        adapter.notifyItemChanged(index)
    }
}
