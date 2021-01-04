package org.viper75.todolistmvvm.ui.tasks

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.viper75.todolistmvvm.R

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.tasks_fragment) {

    private val viewModel: TasksViewModel by viewModels()
}