package com.khdv.habitstracker.presentation.screens.edit

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.khdv.habitstracker.R
import com.khdv.habitstracker.data.repositories.HabitsRepository
import com.khdv.habitstracker.databinding.FragmentEditHabitBinding
import com.khdv.habitstracker.presentation.ActionEventObserver
import com.khdv.habitstracker.presentation.ContentEventObserver
import com.khdv.habitstracker.presentation.HabitsTrackerApplication
import javax.inject.Inject

class EditHabitFragment : Fragment() {

    private val viewModel: EditHabitViewModel by viewModels {
        val delay = resources.getInteger(R.integer.request_delay).toLong()
        EditHabitViewModelFactory(habitsRepository, args.habitId, delay)
    }

    @Inject
    lateinit var habitsRepository: HabitsRepository
    private lateinit var requiredTextFields: List<EditText>
    private lateinit var args: EditHabitFragmentArgs

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as HabitsTrackerApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditHabitBinding.inflate(inflater, container, false)

        args = EditHabitFragmentArgs.fromBundle(requireArguments())

        binding.viewModel = viewModel
        binding.saveButton.setOnClickListener { saveHabit() }

        requiredTextFields = with(binding) { listOf(title, description, quantity, periodicity) }

        binding.lifecycleOwner = this

        setHasOptionsMenu(args.habitId != null)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.error.observe(viewLifecycleOwner,
            ContentEventObserver {
                val message = getString(R.string.connection_error_message)
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            })
        viewModel.returnToHomeScreen.observe(viewLifecycleOwner,
            ActionEventObserver {
                findNavController().navigateUp()
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.habit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.delete_habit -> {
            viewModel.deleteHabit()
            true
        }
        else -> super.onOptionsItemSelected(item)
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
