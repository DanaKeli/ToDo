package com.example.todo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.data.database.entity.Task
import com.example.todo.databinding.ItemTaskInfoBinding
import kotlin.collections.ArrayList

class TaskAdapter : RecyclerView.Adapter<TaskViewHolder>() {

    var onTaskClickListener: OnTaskClickListener? = null
    var onCheckBoxClickListener: OnCheckBoxClickListener? = null
    private lateinit var binding: ItemTaskInfoBinding

    interface OnTaskClickListener {
        fun onTaskClick(position: Int)
    }

    interface OnCheckBoxClickListener {
        fun onCheckBoxClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        binding = ItemTaskInfoBinding.inflate(LayoutInflater.from(parent.context))
        return TaskViewHolder(binding)

    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task: Task = taskInfoList[position]
        holder.bind(task)

        holder.itemView.setOnClickListener {
            View.OnClickListener() {

                @Override
                fun onClick(view: View) {
                    onTaskClickListener?.onTaskClick(position)
                }
            }
        }
    }

    override fun getItemCount(): Int = taskInfoList.size


    @JvmName("setOnTaskClickListener1")
    fun setOnTaskClickListener(onTaskClickListener: OnTaskClickListener) {
        this.onTaskClickListener = onTaskClickListener
    }

    var taskInfoList = listOf<Task>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
}
