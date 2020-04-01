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
import kotlinx.android.synthetic.main.fragment_home.*

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigateToHabitCreation.observe(viewLifecycleOwner, Observer {
            it.executeIfNotHandled(this::navigateToCreateHabit)
        })

        pager.adapter = HabitListPagerAdapter(this)
        TabLayoutMediator(tabs, pager) { tab, position -> tab.text = tabLabels[position] }.attach()

        add_habit_button.setOnClickListener { viewModel.createHabit() }
    }

    private fun navigateToCreateHabit() {
        val action = HomeFragmentDirections.actionHabitListFragmentToEditHabitFragment()
        findNavController().navigate(action)
    }
}
