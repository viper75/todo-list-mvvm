package org.viper75.todolistmvvm.ui.addedittask

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import org.viper75.todolistmvvm.R
import org.viper75.todolistmvvm.databinding.AddEditTaskFragmentBinding
import org.viper75.todolistmvvm.ui.addedittask.AddEditTaskViewModel.*
import org.viper75.todolistmvvm.ui.addedittask.AddEditTaskViewModel.AddEditTaskEvent.*

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.add_edit_task_fragment) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = AddEditTaskFragmentBinding.bind(view)

        binding.apply {
            taskNameEditText.apply {
                setText(viewModel.taskName)
                addTextChangedListener {
                    viewModel.taskName = it.toString()
                }
            }

            importantCheckBox.apply {
                isChecked = viewModel.taskImportant
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.taskImportant = isChecked
                }
            }

            dateCreatedTextView.apply {
                isVisible = viewModel.task != null
                text = "Created: ${viewModel.task?.createdDateFormatted}"
            }

            saveTaskFab.setOnClickListener {
                viewModel.onSaveClicked()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when (event) {
                    is ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT)
                    }

                    is NavigateBackWithResult -> {
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )

                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}