package org.viper75.todolistmvvm.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.viper75.todolistmvvm.R
import org.viper75.todolistmvvm.databinding.TasksFragmentBinding
import org.viper75.todolistmvvm.utils.onQueryTextChanged

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

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu)

        val searchItem = menu.findItem(R.id.search_action)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.sort_by_name_action -> {
                true
            }

            R.id.sort_by_date_created_action -> {
                true
            }

            R.id.hide_completed_tasks_action -> {
                true
            }

            R.id.delete_all_completed_tasks_action -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}