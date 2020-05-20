package com.khdv.habitstracker.presentation.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.khdv.habitstracker.R
import com.khdv.habitstracker.data.db.HabitsTrackerDatabase
import com.khdv.habitstracker.data.network.HabitsApi
import com.khdv.habitstracker.data.repositories.HabitsRepository
import com.khdv.habitstracker.databinding.FragmentHabitFiltersAndSortsBinding

class HabitFiltersAndSortsFragment : Fragment() {

    private val viewModel: HabitsViewModel by activityViewModels {
        val dao = HabitsTrackerDatabase.getInstance(requireContext()).habitDao()
        val delay = resources.getInteger(R.integer.request_delay).toLong()
        HabitsViewModelFactory(HabitsRepository(dao, HabitsApi.service), delay)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHabitFiltersAndSortsBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}