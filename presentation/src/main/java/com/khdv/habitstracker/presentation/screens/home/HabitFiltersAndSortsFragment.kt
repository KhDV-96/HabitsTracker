package com.khdv.habitstracker.presentation.screens.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.khdv.habitstracker.R
import com.khdv.habitstracker.databinding.FragmentHabitFiltersAndSortsBinding
import com.khdv.habitstracker.presentation.HabitsTrackerApplication
import javax.inject.Inject

class HabitFiltersAndSortsFragment : Fragment() {

    private val viewModel: HabitsViewModel by activityViewModels { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as HabitsTrackerApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHabitFiltersAndSortsBinding.inflate(inflater, container, false)

        viewModel.initialize(resources.getInteger(R.integer.request_delay).toLong())

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}