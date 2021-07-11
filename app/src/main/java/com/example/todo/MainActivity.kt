package com.example.todo

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.adapter.TaskAdapter
import com.example.todo.data.api.ApiHelperImpl
import com.example.todo.data.api.RetrofitBuilder
import com.example.todo.data.database.DBBuilder
import com.example.todo.data.database.DBHelperImpl
import com.example.todo.data.database.TasksDataBase
import com.example.todo.data.database.entity.Task
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.notification.Notification
import com.example.todo.utils.Status
import com.example.todo.utils.ViewModelFactory
import com.example.todo.viewModels.NotDoneTasksViewModel
import com.example.todo.viewModels.TaskViewModel
import java.time.LocalDate
import java.time.LocalTime

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var adapter = TaskAdapter()
    private var taskList = listOf<Task>()
    private var isDOneVisible = false
    private var tasksAmountToday: Int? = null
    lateinit var manager: NotificationManager
    lateinit var db: TasksDataBase
    lateinit var viewModel: TaskViewModel
    lateinit var notDoneTasksViewModel: NotDoneTasksViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        db = DBBuilder.getInstance(this)
        appContext = this

        setUpUI()
        setupViewModel()
        setupObserver()
    }

    private fun setUpUI() {
        binding.rvTasks.layoutManager = GridLayoutManager(this,1)
        binding.rvTasks.adapter = TaskAdapter()
        renderList(taskList)

        binding.tvDone.text = "Выполнено - ${taskList.count { it.isDone }}"

        val obj = object : TaskAdapter.OnTaskClickListener {
            override fun onTaskClick(position: Int) {
                val intent = Intent(applicationContext, TaskItemActivity::class.java)
                intent.putExtra("note", taskList[position] as Parcelable)
                startActivity(intent)
            }
        }
        adapter.setOnTaskClickListener(obj)
        leftSwipe()
        rightSwipe()

        tasksAmountToday = taskList.filterNot { it.isDone }.count { LocalDate.parse(it.date) == LocalDate.now() }
        if (tasksAmountToday != 0 && LocalTime.now() >= LocalTime.of(8, 0)) {

        }
    }

    private fun renderList(tasks: List<Task>) {
        adapter.taskInfoList = tasks
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this,
        ViewModelFactory(
            ApiHelperImpl(RetrofitBuilder.apiService),
            DBHelperImpl(DBBuilder.getInstance(appContext))
        )
        ).get(TaskViewModel::class.java)
    }

    private fun setupObserver() {
        if (!isDOneVisible) {
        viewModel.getAllTasks().observe(this, {
            when(it.status) {
                Status.SUCCESS -> {
                    it.data?.let { taskList -> renderList(taskList) }

                }
            }
        })} else {
            notDoneTasksViewModel.getAllTasks().observe(this, {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { taskList -> renderList(taskList) }

                    }
                }
            }
            )
        }
    }

    private fun leftSwipe() {
        val leftCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.taskInfoList[viewHolder.adapterPosition]
                adapter.notifyDataSetChanged()
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        val leftHelper = ItemTouchHelper(leftCallback)
        leftHelper.attachToRecyclerView(binding.rvTasks)
    }

    private fun rightSwipe() {
        val leftCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.taskInfoList[viewHolder.adapterPosition].isDone = true
                adapter.notifyDataSetChanged()
            }

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                    isCurrentlyActive
                )
            }
        }
        val leftHelper = ItemTouchHelper(leftCallback)
        leftHelper.attachToRecyclerView(binding.rvTasks)
    }



    fun onClickAdd(view: View) {
        val intent = Intent(this, TaskItemActivity::class.java)
        startActivity(intent)
    }

    fun onClickDoneVisibility(view: View) {
        if (isDOneVisible) {
            binding.ivDoneVisibility.setColorFilter(
                applicationContext.resources.getColor(R.color.gray_light),
                PorterDuff.Mode.SRC_IN
            )
            adapter.taskInfoList =
                ArrayList<Task>((taskList.filterNot { it.isDone }.sortedBy { note -> note.priority }
                    .sortedBy { note -> note.date }))
            isDOneVisible = false
            adapter.notifyDataSetChanged()
        } else {
            binding.ivDoneVisibility.setColorFilter(
                applicationContext.resources.getColor(R.color.blue),
                PorterDuff.Mode.SRC_IN
            )
            adapter.taskInfoList = ArrayList<Task>(taskList.sortedBy { note -> note.priority }
                .sortedBy { note -> note.date })
            isDOneVisible = true
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "com.example.todo"
        lateinit var appContext: Context
    }
}