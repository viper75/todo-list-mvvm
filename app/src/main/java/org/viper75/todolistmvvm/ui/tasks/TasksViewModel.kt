package org.viper75.todolistmvvm.ui.tasks

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.viper75.todolistmvvm.data.PreferencesManager
import org.viper75.todolistmvvm.data.SortOrder
import org.viper75.todolistmvvm.data.Task
import org.viper75.todolistmvvm.data.TaskDao
import org.viper75.todolistmvvm.ui.ADD_TASK_RESULT_OK
import org.viper75.todolistmvvm.ui.EDIT_TASK_RESULT_OK

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
    @Assisted private val state: SavedStateHandle
): ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEventFlow = tasksEventChannel.receiveAsFlow()

    private val tasksFlow = combine(searchQuery.asFlow(), preferencesFlow) {
        searchQuery, filterPreferences -> Pair(searchQuery, filterPreferences)
    }.flatMapLatest { (searchQuery, filterPreferences) ->
        taskDao.getAllTasks(searchQuery, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    val tasks =  tasksFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) =viewModelScope.launch {
            preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedChecked(hideCompleted: Boolean) = viewModelScope.launch {
            preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onItemClick(task: Task) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditScreen(task))
    }

    fun onItemChecked(task: Task, isChecked: Boolean) = viewModelScope.launch {
        taskDao.update(task.copy(completed = isChecked))
    }

    fun onItemSwiped(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDelete(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

    fun onAddNewTaskClicked() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task Added")
            EDIT_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task Updated")
        }
    }

    private fun showTaskSavedConfirmationMessage(msg: String) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(msg))
    }

    fun onDeleteAllCompletedTasks() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToDeleteAllScreen)
    }

    sealed class TasksEvent {
        object NavigateToAddTaskScreen : TasksEvent()
        object NavigateToDeleteAllScreen : TasksEvent()

        data class NavigateToEditScreen(
            val task: Task
        ) : TasksEvent()

        data class ShowUndoDeleteTaskMessage(
            val task: Task
        ) : TasksEvent()

        data class ShowTaskSavedConfirmationMessage (
            val msg: String
        ) : TasksEvent()
    }
}

