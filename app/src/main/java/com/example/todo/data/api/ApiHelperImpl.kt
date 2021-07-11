package com.example.todo.data.api

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override suspend fun getTasks(): List<ApiTask> = apiService.getTasks()

    override suspend fun getTasksWithError(): List<ApiTask> = apiService.getTasksWithError()

}