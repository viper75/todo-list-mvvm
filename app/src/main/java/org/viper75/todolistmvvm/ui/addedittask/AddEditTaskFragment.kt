package org.viper75.todolistmvvm.ui.addedittask

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.viper75.todolistmvvm.R
import org.viper75.todolistmvvm.databinding.AddEditTaskFragmentBinding

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.add_edit_task_fragment) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = AddEditTaskFragmentBinding.bind(view)

        binding.apply {
            taskNameEditText.setText(viewModel.taskName)
            importantCheckBox.apply {
                isChecked = viewModel.taskImportant
                jumpDrawablesToCurrentState()
            }
            dateCreatedTextView.apply {
                isVisible = viewModel.task != null
                text = "Created: ${viewModel.task?.createdDateFormatted}"
            }
        }
    }
}