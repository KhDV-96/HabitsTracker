package com.khdv.habitstracker.screens.home

import android.content.Context
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
import com.khdv.habitstracker.data.HabitsRepository
import com.khdv.habitstracker.databinding.FragmentHabitListBinding
import com.khdv.habitstracker.db.HabitsTrackerDatabase
import com.khdv.habitstracker.model.Habit
import com.khdv.habitstracker.util.ContentEventObserver

class HabitListFragment : Fragment() {

    companion object {
        private const val HABIT_TYPE_ARGUMENT = "HABIT_TYPE"

        @JvmStatic
        fun newInstance(habitType: Habit.Type) = HabitListFragment().apply {
            arguments = bundleOf(HABIT_TYPE_ARGUMENT to habitType.name)
        }
    }

    private val viewModel: HabitsViewModel by activityViewModels {
        val dao = HabitsTrackerDatabase.getInstance(requireContext()).habitDao()
        HabitsViewModelFactory(HabitsRepository(dao))
    }
    private lateinit var listDecoration: DividerItemDecoration
    private lateinit var adapter: HabitsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHabitListBinding.inflate(inflater, container, false)

        adapter = HabitsAdapter(HabitClickListener(viewModel::editHabit))

        binding.apply {
            habitList.adapter = adapter
            habitList.addItemDecoration(listDecoration)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = Habit.Type.valueOf(arguments!!.getString(HABIT_TYPE_ARGUMENT)!!)

        viewModel.getHabitsWithType(type).observe(viewLifecycleOwner, Observer(adapter::submitList))
        viewModel.navigateToHabitEditing.observe(viewLifecycleOwner, ContentEventObserver {
            navigateToEditHabit(it)
        })
    }

    private fun navigateToEditHabit(habitId: Int) {
        val action = HomeFragmentDirections.actionHabitListFragmentToEditHabitFragment(habitId)
        findNavController().navigate(action)
    }
}
