package com.khdv.habitstracker.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.khdv.habitstracker.R
import com.khdv.habitstracker.data.HabitsRepository
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

    private val viewModel: HabitsViewModel by activityViewModels {
        HabitsViewModelFactory(HabitsRepository())
    }
    private lateinit var adapter: HabitsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = Habit.Type.valueOf(arguments!!.getString(HABIT_TYPE_ARGUMENT)!!)

        adapter = HabitsAdapter(HabitClickListener(viewModel::editHabit))

        viewModel.getHabitsWithType(type).observe(viewLifecycleOwner, Observer(adapter::submitList))
        viewModel.navigateToHabitEditing.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let(this::navigateToEditHabit)
        })

        habit_list.adapter = adapter
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        habit_list.addItemDecoration(decoration)
    }

    private fun navigateToEditHabit(habitId: Int) {
        val action = HomeFragmentDirections.actionHabitListFragmentToEditHabitFragment(habitId)
        findNavController().navigate(action)
    }
}
