package org.viper75.todolistmvvm.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import org.viper75.todolistmvvm.api.TodosApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val todosApiService: TodosApiService,
    private val taskDao: TaskDao
) {

    suspend fun getAllTasks(searchQuery: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> {
        val task = taskDao.getFirstTask()

        val isLocalDataExpired = if (task == null) true else task.created.plus(20000) < System.currentTimeMillis()

        if (isLocalDataExpired) {
            val tasks = todosApiService.getAllTasks()
            Log.i(TaskRepository::class.java.canonicalName, "Response -> $tasks")
            taskDao.insertAll(tasks)
        }

        return when(sortOrder) {
            SortOrder.BY_NAME -> taskDao.getAllTaskSortedByName(searchQuery, hideCompleted)
            SortOrder.BY_DATE -> taskDao.getAllTaskSortedByDateCreated(searchQuery, hideCompleted)
        }
    }

    suspend fun insertTask(task: Task) {
        try {
            val newTask = todosApiService.addTask(task)
            taskDao.insert(newTask)
        } catch (ioe: IOException) {
            throw ioe
        } catch (he: HttpException) {
            throw he
        }
    }

    suspend fun updateTask(task: Task) {
        try {
            val updatedTask = todosApiService.updateTask(task)
            taskDao.insert(updatedTask)
        } catch (ioe: IOException) {
            throw ioe
        } catch (he: HttpException) {
            throw he
        }
    }

    suspend fun deleteTask(task: Task) {
        try {
            todosApiService.deleteTask(task)
            taskDao.delete(task)
        } catch (ioe: IOException) {
            throw ioe
        } catch (he: HttpException) {
            throw he
        }
    }

    suspend fun deleteAllCompleted() {
        taskDao.deleteAllCompleted()
    }
}