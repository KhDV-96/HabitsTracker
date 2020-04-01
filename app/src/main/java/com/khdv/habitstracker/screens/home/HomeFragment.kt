package com.khdv.habitstracker.screens.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.khdv.habitstracker.R
import com.khdv.habitstracker.data.HabitsRepository
import com.khdv.habitstracker.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val viewModel: HabitsViewModel by activityViewModels {
        HabitsViewModelFactory(HabitsRepository())
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

        viewModel.navigateToHabitCreation.observe(viewLifecycleOwner, Observer {
            it.executeIfNotHandled(this::navigateToCreateHabit)
        })
    }

    private fun navigateToCreateHabit() {
        val action = HomeFragmentDirections.actionHabitListFragmentToEditHabitFragment()
        findNavController().navigate(action)
    }
}