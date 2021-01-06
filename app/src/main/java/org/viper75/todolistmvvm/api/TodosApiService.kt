package org.viper75.todolistmvvm.api

import org.viper75.todolistmvvm.data.Task
import retrofit2.http.*

interface TodosApiService {

    companion object {
        const val API_BASE_URL = "https://todos-api-service.herokuapp.com/"
    }

    @GET("tasks")
    suspend fun getAllTasks(): List<Task>

    @POST("tasks")
    suspend fun addTask(@Body task: Task): Task

    @PATCH("tasks")
    suspend fun updateTask(@Body task: Task): Task

    @DELETE("tasks")
    suspend fun deleteTask(task: Task)
}