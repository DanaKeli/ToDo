package com.example.todo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.api.ApiHelper
import com.example.todo.data.database.DBHelper
import com.example.todo.data.database.entity.Task
import com.example.todo.utils.Resource
import kotlinx.coroutines.launch

class NotDoneTasksViewModel(private val apiHelper: ApiHelper, private val dbHelper: DBHelper) :
    ViewModel() {

    private val notDoneTasks = MutableLiveData<Resource<List<Task>>>()

    init {
        receiveTasks()
    }

    private fun receiveTasks() {
        viewModelScope.launch {
            val tasksFromDB = dbHelper.getNotDoneTasks()
            notDoneTasks.postValue(Resource.success(tasksFromDB))
            }
        }

    fun getAllTasks(): LiveData<Resource<List<Task>>> {
        return notDoneTasks
    }
}