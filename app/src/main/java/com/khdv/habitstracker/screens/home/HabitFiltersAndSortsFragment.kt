package com.khdv.habitstracker.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.khdv.habitstracker.data.HabitsRepository
import com.khdv.habitstracker.databinding.FragmentHabitFiltersAndSortsBinding
import com.khdv.habitstracker.db.HabitsTrackerDatabase

class HabitFiltersAndSortsFragment : Fragment() {

    private val viewModel: HabitsViewModel by activityViewModels {
        val dao = HabitsTrackerDatabase.getInstance(requireContext()).habitDao()
        HabitsViewModelFactory(HabitsRepository(dao))
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