package org.viper75.todolistmvvm.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.viper75.todolistmvvm.R
import org.viper75.todolistmvvm.databinding.TasksFragmentBinding

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.tasks_fragment) {

    private val viewModel: TasksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = TasksFragmentBinding.bind(view)

        val listAdapter = TasksListAdapter()

        binding.apply {
            tasksRecyclerView.apply {
                this.adapter = listAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
    }
}