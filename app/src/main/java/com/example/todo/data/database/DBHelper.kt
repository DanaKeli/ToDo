package com.example.todo.data.database

import com.example.todo.data.database.entity.Task

interface DBHelper {
    suspend fun getAllTasks(): List<Task>

    suspend fun getNotDoneTasks(): List<Task>

    suspend fun insertAll(tasks: List<Task>)

    suspend fun deleteAllTasks()

    suspend fun insertTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)
}