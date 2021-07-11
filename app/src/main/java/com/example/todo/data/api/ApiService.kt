package com.example.todo.data.api

import com.example.todo.data.database.entity.Task
import retrofit2.http.GET
import retrofit2.http.PUT

interface ApiService {

    @GET("/tasks/")
    suspend fun getTasks(): List<ApiTask>

    @GET("error")
    suspend fun getTasksWithError(): List<ApiTask>

    @PUT("/tasks/task_id")
    suspend fun updateTask()
}