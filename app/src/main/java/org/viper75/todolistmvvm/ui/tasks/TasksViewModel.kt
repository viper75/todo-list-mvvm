package org.viper75.todolistmvvm.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import org.viper75.todolistmvvm.data.TaskDao

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao
): ViewModel() {

    val searchQuery = MutableStateFlow("")

    private val tasksFlow = searchQuery.flatMapLatest {
        taskDao.getAllTasks(it)
    }

    val tasks =  tasksFlow.asLiveData()
}