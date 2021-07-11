package com.example.todo.data.database.dao

import androidx.room.*
import com.example.todo.data.database.entity.Task

@Dao
interface TasksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Insert
    suspend fun insertAll(tasks: List<Task>)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks ORDER BY date IS NULL, date ASC, priority DESC")
    suspend fun getAllTasks(): List<Task>

    @Query("SELECT * FROM tasks WHERE isDone == 0 ORDER BY date IS NULL, date ASC, priority DESC")
    suspend fun getNotDoneTasks(): List<Task>

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()

}