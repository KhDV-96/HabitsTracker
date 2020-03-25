package com.khdv.habitstracker.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.khdv.habitstracker.MainActivity
import com.khdv.habitstracker.R
import com.khdv.habitstracker.model.Habit
import kotlinx.android.synthetic.main.fragment_habit_list.*

class HabitListFragment : Fragment() {

    companion object {
        private const val HABIT_TYPE_ARGUMENT = "HABIT_TYPE"

        @JvmStatic
        fun newInstance(habitType: Habit.Type) = HabitListFragment().apply {
            arguments = bundleOf(HABIT_TYPE_ARGUMENT to habitType.name)
        }
    }

    private lateinit var habits: List<Habit>
    private lateinit var filteredHabits: List<Habit>
    private lateinit var adapter: HabitsAdapter
    private lateinit var habitType: Habit.Type

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        habits = (activity as MainActivity).habits
        habitType = Habit.Type.valueOf(arguments!!.getString(HABIT_TYPE_ARGUMENT)!!)
        filteredHabits = habits.filter { it.type == habitType }
        adapter = HabitsAdapter(filteredHabits, HabitClickListener(::navigateToEditHabit))

        return inflater.inflate(R.layout.fragment_habit_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habit_list.adapter = adapter
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        habit_list.addItemDecoration(decoration)
    }

    private fun navigateToEditHabit(habit: Habit) {
        val index = habits.indexOf(habit)
        val action = HomeFragmentDirections.actionHabitListFragmentToEditHabitFragment(index)
        findNavController().navigate(action)
    }
}
