package com.khdv.habitstracker.presentation.screens.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.khdv.habitstracker.R
import com.khdv.habitstracker.data.repositories.HabitsRepository
import com.khdv.habitstracker.databinding.FragmentHabitFiltersAndSortsBinding
import com.khdv.habitstracker.presentation.HabitsTrackerApplication
import javax.inject.Inject

class HabitFiltersAndSortsFragment : Fragment() {

    private val viewModel: HabitsViewModel by activityViewModels {
        val delay = resources.getInteger(R.integer.request_delay).toLong()
        HabitsViewModelFactory(habitsRepository, delay)
    }

    @Inject
    lateinit var habitsRepository: HabitsRepository

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as HabitsTrackerApplication).appComponent.inject(this)
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