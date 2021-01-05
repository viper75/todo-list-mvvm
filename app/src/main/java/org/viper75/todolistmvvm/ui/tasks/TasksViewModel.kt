package org.viper75.todolistmvvm.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.viper75.todolistmvvm.data.PreferencesManager
import org.viper75.todolistmvvm.data.SortOrder
import org.viper75.todolistmvvm.data.TaskDao

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager
): ViewModel() {

    val searchQuery = MutableStateFlow("")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksFlow = combine(searchQuery, preferencesFlow) {
        searchQuery, preferencesFlow -> Pair(searchQuery, preferencesFlow)
    }.flatMapLatest { (searchQuery, preferencesFlow) ->
        taskDao.getAllTasks(searchQuery, preferencesFlow.sortOrder, preferencesFlow.hideCompleted)
    }

    val tasks =  tasksFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) {
        viewModelScope.launch {
            preferencesManager.updateSortOrder(sortOrder)
        }
    }

    fun onHideCompletedChecked(hideCompleted: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateHideCompleted(hideCompleted)
        }
    }
}

