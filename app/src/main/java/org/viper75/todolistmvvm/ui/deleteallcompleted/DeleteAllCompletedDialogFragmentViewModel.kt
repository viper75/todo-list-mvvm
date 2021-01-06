package org.viper75.todolistmvvm.ui.deleteallcompleted

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.viper75.todolistmvvm.data.TaskDao
import org.viper75.todolistmvvm.data.TaskRepository
import org.viper75.todolistmvvm.di.ApplicationScope

class DeleteAllCompletedDialogFragmentViewModel @ViewModelInject constructor(
    private val taskRepository: TaskRepository,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    fun deleteAllCompletedTasks() = applicationScope.launch {
        taskRepository.deleteAllCompleted()
    }
}