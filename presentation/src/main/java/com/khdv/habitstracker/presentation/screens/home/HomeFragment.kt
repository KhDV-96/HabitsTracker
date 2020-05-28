package com.khdv.habitstracker.presentation.screens.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.khdv.habitstracker.R
import com.khdv.habitstracker.databinding.FragmentHomeBinding
import com.khdv.habitstracker.domain.interactors.LoadHabitsUseCase
import com.khdv.habitstracker.domain.interactors.RepeatHabitUseCase
import com.khdv.habitstracker.presentation.ActionEventObserver
import com.khdv.habitstracker.presentation.HabitsTrackerApplication
import javax.inject.Inject

class HomeFragment : Fragment() {

    private val viewModel: HabitsViewModel by activityViewModels {
        val delay = resources.getInteger(R.integer.request_delay).toLong()
        HabitsViewModelFactory(loadHabitsUseCase, repeatHabitUseCase, delay)
    }

    @Inject
    lateinit var loadHabitsUseCase: LoadHabitsUseCase

    @Inject
    lateinit var repeatHabitUseCase: RepeatHabitUseCase

    private lateinit var tabLabels: Array<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as HabitsTrackerApplication).appComponent.inject(this)
        tabLabels = arrayOf(getString(R.string.useful_label), getString(R.string.harmful_label))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.pager.adapter = HabitListPagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = tabLabels[position]
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigateToHabitCreation.observe(viewLifecycleOwner,
            ActionEventObserver {
                navigateToCreateHabit()
            })
    }

    private fun navigateToCreateHabit() {
        val action = HomeFragmentDirections.actionHabitListFragmentToEditHabitFragment()
        findNavController().navigate(action)
    }
}