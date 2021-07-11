package com.example.todo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todo.data.database.entity.Task
import com.example.todo.data.database.dao.TasksDao

@Database(entities = [Task::class], version = 2, exportSchema = false)
abstract class TasksDataBase : RoomDatabase() {

   abstract fun taskDao(): TasksDao
}