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

class TaskViewModel(private val apiHelper: ApiHelper, private val dbHelper: DBHelper) :
    ViewModel() {

    private val tasks = MutableLiveData<Resource<List<Task>>>()

    init {
        receiveTasks()
    }

    private fun receiveTasks() {
        viewModelScope.launch {
            tasks.postValue(Resource.loading(null))
            val tasksFromDB = dbHelper.getAllTasks()
            try {
                val tasksFromApi = apiHelper.getTasks()
                val tasksToInsertInDB = mutableListOf<Task>()

                for (apiTask in tasksFromApi) {
                    val task = Task(
                        apiTask._id,
                        apiTask.description,
                        apiTask.priority,
                        apiTask.isDone,
                        apiTask.date.toString(),
                        apiTask.createdAt.toString(),
                        apiTask.updatedAt.toString()
                    )
                    tasksToInsertInDB.add(task)
                }
                dbHelper.insertAll(tasksToInsertInDB)
                tasks.postValue(Resource.success(tasksToInsertInDB))
            } catch (e: Exception) {
                tasks.postValue(Resource.success(tasksFromDB))
            }
        }
    }

    fun getAllTasks(): LiveData<Resource<List<Task>>> {
        return tasks
    }
}

