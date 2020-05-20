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
import com.khdv.habitstracker.data.db.HabitsTrackerDatabase
import com.khdv.habitstracker.data.network.HabitsApi
import com.khdv.habitstracker.data.repositories.HabitsRepository
import com.khdv.habitstracker.databinding.FragmentHomeBinding
import com.khdv.habitstracker.presentation.ActionEventObserver

class HomeFragment : Fragment() {

    private val viewModel: HabitsViewModel by activityViewModels {
        val dao = HabitsTrackerDatabase.getInstance(requireContext()).habitDao()
        val delay = resources.getInteger(R.integer.request_delay).toLong()
        HabitsViewModelFactory(HabitsRepository(dao, HabitsApi.service), delay)
    }
    private lateinit var tabLabels: Array<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
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