package com.example.todo.data.database

import com.example.todo.data.database.entity.Task

class DBHelperImpl(private val tasksDataBase: TasksDataBase) : DBHelper {

    override suspend fun getAllTasks(): List<Task> =
        tasksDataBase.taskDao().getAllTasks()

    override suspend fun getNotDoneTasks(): List<Task> =
        tasksDataBase.taskDao().getNotDoneTasks()

    override suspend fun insertAll(tasks: List<Task>) =
        tasksDataBase.taskDao().insertAll(tasks)

    override suspend fun deleteAllTasks() =
        tasksDataBase.taskDao().deleteAllTasks()

    override suspend fun insertTask(task: Task) =
        tasksDataBase.taskDao().insertTask(task)

    override suspend fun updateTask(task: Task) =
        tasksDataBase.taskDao().updateTask(task)

    override suspend fun deleteTask(task: Task) =
        tasksDataBase.taskDao().deleteTask(task)
}