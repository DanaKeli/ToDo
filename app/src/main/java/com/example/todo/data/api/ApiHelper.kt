package com.example.todo.data.api

interface ApiHelper {

    suspend fun getTasks(): List<ApiTask>

    suspend fun getTasksWithError(): List<ApiTask>
}