package org.viper75.todolistmvvm.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.viper75.todolistmvvm.data.Task
import org.viper75.todolistmvvm.data.TaskRepository
import org.viper75.todolistmvvm.ui.ADD_TASK_RESULT_OK
import org.viper75.todolistmvvm.ui.EDIT_TASK_RESULT_OK

class AddEditTaskViewModel @ViewModelInject constructor(
    private val taskRepository: TaskRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskImportant = state.get<Boolean>("taskImportant") ?: task?.important ?: false
        set(value) {
            field = value
            state.set("taskImportant", value)
        }

    private val addEditTaskChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskChannel.receiveAsFlow()

    fun onSaveClicked() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Name is required.")
            return
        }

        if (task != null) {
            val updatedTask = task.copy(name = taskName, important = taskImportant)
            updateTask(updatedTask)
        } else {
            val newTask = Task(name = taskName, important = taskImportant)
            addTask(newTask)
        }
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditTaskChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(text))
    }

    private fun addTask(task: Task) = viewModelScope.launch {
        taskRepository.insertTask(task)
        addEditTaskChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        taskRepository.updateTask(task)
        addEditTaskChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(
            val msg: String
        ) : AddEditTaskEvent()

        data class NavigateBackWithResult(
            val result: Int
        ) : AddEditTaskEvent()
    }
}