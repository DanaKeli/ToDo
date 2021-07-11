package com.example.todo.data.database

import android.content.Context
import androidx.room.Room

object DBBuilder {
    private var db: TasksDataBase? = null
    private const val DB_NAME = "tasks.db"
    private val LOCK = Any()

    fun getInstance(context: Context): TasksDataBase {
        synchronized(LOCK) {
            db?.let { return it}
            val instance: TasksDataBase = Room.databaseBuilder(
                context,
                TasksDataBase::class.java,
                DB_NAME
            ).build()
            db = instance
            return instance
        }
    }
}