package com.khdv.habitstracker.presentation.screens.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.khdv.habitstracker.R
import com.khdv.habitstracker.data.network.HabitsApiException
import com.khdv.habitstracker.databinding.FragmentHabitListBinding
import com.khdv.habitstracker.domain.models.Habit
import com.khdv.habitstracker.presentation.ContentEventObserver
import com.khdv.habitstracker.presentation.HabitsTrackerApplication
import java.io.IOException
import javax.inject.Inject

class HabitListFragment : Fragment() {

    companion object {
        private const val HABIT_TYPE_ARGUMENT = "HABIT_TYPE"

        @JvmStatic
        fun newInstance(habitType: Habit.Type) = HabitListFragment().apply {
            arguments = bundleOf(HABIT_TYPE_ARGUMENT to habitType.name)
        }
    }

    private val viewModel: HabitsViewModel by activityViewModels { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var listDecoration: DividerItemDecoration
    private lateinit var adapter: HabitsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as HabitsTrackerApplication).appComponent.inject(this)
        listDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHabitListBinding.inflate(inflater, container, false)

        adapter = HabitsAdapter(
            HabitClickListener(viewModel::editHabit),
            HabitDoneListener(viewModel::repeatHabit)
        )

        binding.apply {
            habitList.adapter = adapter
            habitList.addItemDecoration(listDecoration)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = Habit.Type.valueOf(requireArguments().getString(HABIT_TYPE_ARGUMENT)!!)

        viewModel.initialize(resources.getInteger(R.integer.request_delay).toLong())
        viewModel.getHabitsWithType(type).observe(viewLifecycleOwner, Observer(adapter::submitList))
        viewModel.showRepeatingStatus.observe(
            viewLifecycleOwner,
            ContentEventObserver { (habit, remainingReps) ->
                showHabitRepeatingStatus(habit.type, remainingReps)
            }
        )
        viewModel.navigateToHabitEditing.observe(viewLifecycleOwner, ContentEventObserver {
            navigateToEditHabit(it)
        })
        viewModel.error.observe(viewLifecycleOwner, ContentEventObserver {
            val message = when (it) {
                is IOException -> getString(R.string.connection_error_message)
                is HabitsApiException -> getString(R.string.api_error_message, it.code, it.message)
                else -> it.localizedMessage
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        })
    }

    private fun showHabitRepeatingStatus(type: Habit.Type, remainingReps: Int) {
        val message = when {
            type == Habit.Type.USEFUL && remainingReps > 0 ->
                getString(R.string.repeat_useful_habit, remainingReps)
            type == Habit.Type.USEFUL -> getString(R.string.stop_useful_habit)
            type == Habit.Type.HARMFUL && remainingReps > 0 ->
                getString(R.string.repeat_harmful_habit, remainingReps)
            type == Habit.Type.HARMFUL -> getString(R.string.stop_harmful_habit)
            else -> return
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun navigateToEditHabit(habitId: String) {
        val action = HomeFragmentDirections.actionHabitListFragmentToEditHabitFragment(habitId)
        findNavController().navigate(action)
    }
}
