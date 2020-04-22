package com.khdv.habitstracker.screens.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.khdv.habitstracker.R
import com.khdv.habitstracker.data.HabitsRepository
import com.khdv.habitstracker.databinding.FragmentEditHabitBinding
import com.khdv.habitstracker.db.HabitsTrackerDatabase
import com.khdv.habitstracker.network.HabitsApi
import com.khdv.habitstracker.util.ActionEventObserver
import com.khdv.habitstracker.util.ContentEventObserver

class EditHabitFragment : Fragment() {

    private val viewModel: EditHabitViewModel by viewModels {
        val dao = HabitsTrackerDatabase.getInstance(requireContext()).habitDao()
        val args = EditHabitFragmentArgs.fromBundle(requireArguments())
        EditHabitViewModelFactory(HabitsRepository(dao, HabitsApi.service), args.habitId)
    }
    private lateinit var requiredTextFields: List<EditText>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditHabitBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.saveButton.setOnClickListener { saveHabit() }

        requiredTextFields = with(binding) { listOf(title, description, quantity, periodicity) }

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.error.observe(viewLifecycleOwner, ContentEventObserver {
            val message = getString(R.string.connection_error_message)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        })
        viewModel.returnToHomeScreen.observe(viewLifecycleOwner, ActionEventObserver {
            findNavController().navigateUp()
        })
    }

    private fun saveHabit() {
        if (verifyFields())
            viewModel.saveHabit()
    }

    private fun verifyFields(): Boolean {
        requiredTextFields.firstOrNull { it.text.isEmpty() }?.apply {
            error = getString(R.string.required_error)
            return false
        }
        return true
    }
}
