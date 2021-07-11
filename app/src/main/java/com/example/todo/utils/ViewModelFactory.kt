package com.example.todo.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo.viewModels.TaskViewModel
import com.example.todo.data.api.ApiHelper
import com.example.todo.data.database.DBHelper
import com.example.todo.viewModels.NotDoneTasksViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val apiHelper: ApiHelper, private val dbHelper: DBHelper) :
   ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(apiHelper, dbHelper) as T
        }

        if (modelClass.isAssignableFrom(NotDoneTasksViewModel::class.java)) {
            return NotDoneTasksViewModel(apiHelper, dbHelper) as T
        }
        throw IllegalArgumentException("")
    }
}