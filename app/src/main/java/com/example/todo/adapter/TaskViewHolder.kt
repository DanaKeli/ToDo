package com.example.todo.adapter

import android.graphics.Color
import android.graphics.Paint
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.database.entity.Task
import com.example.todo.databinding.ItemTaskInfoBinding
import java.time.LocalDate

class TaskViewHolder(private val binding: ItemTaskInfoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val taskAdapter = TaskAdapter()

    fun bind(task: Task) {

        if (task.isDone) {
            binding.imageViewCheckBox.setImageDrawable(
                ContextCompat.getDrawable(itemView.context, R.drawable.ic_checked)
            )
            strike(task)
        } else {
            redOrPlain(task)
            viewOfPriority(task)
        }

        binding.textViewDateItem.text = task.date.toString()
        if (task.date == null) {
            binding.textViewDateItem.isInvisible
        }

        binding.imageViewInfo.setOnClickListener {
            taskAdapter.onTaskClickListener?.onTaskClick(adapterPosition)
        }

        binding.imageViewCheckBox.setOnClickListener {
            taskAdapter.onCheckBoxClickListener?.onCheckBoxClick(adapterPosition)
            if (task.isDone) {
                redOrPlain(task)
                task.isDone = false
                viewOfPriority(task)
                binding.textViewDescriptionMain.paintFlags = 0
                binding.textViewDescriptionMain.setTextColor(Color.GRAY)
            } else {
                binding.imageViewCheckBox.setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_checked)
                )
                task.isDone = true
                strike(task)
            }
        }
    }

    private fun viewOfPriority(task: Task) {
        when (task.priority) {
            "high" -> binding.textViewDescriptionMain.setText(
                exclamation(task), TextView.BufferType.SPANNABLE
            )
            "low" -> binding.textViewDescriptionMain.setText(
                arrow(task), TextView.BufferType.SPANNABLE
            )
            else -> plain(task)
        }
    }

    private fun redOrPlain(task: Task) {
        if (isDeadlineOff(task)) {
            binding.imageViewCheckBox.setImageDrawable(
                ContextCompat.getDrawable(itemView.context, R.drawable.unchecked_red)
            )
        } else {
            binding.imageViewCheckBox.setImageDrawable(
                ContextCompat.getDrawable(itemView.context, R.drawable.unchecked)
            )
        }
    }

    private fun plain(task: Task) {
        binding.textViewDescriptionMain.text = task.description
        binding.textViewDescriptionMain.setTextColor(Color.GRAY)
        binding.textViewDescriptionMain.paintFlags = 0
    }

    private fun strike(task: Task) {
        binding.textViewDescriptionMain.text = task.description
        binding.textViewDescriptionMain.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        binding.textViewDescriptionMain.setTextColor(itemView.resources.getColor(R.color.gray_light))
    }

    private fun arrow(task: Task): SpannableStringBuilder {
        val line = SpannableStringBuilder()
        val low = SpannableString("↓")
        low.setSpan(
            ForegroundColorSpan(Color.GRAY),
            0, low.length, 0
        )
        line.bold { append(low) }
        line.append(" ")
        line.append(task.description)
        return line
    }

    private fun exclamation(task: Task): SpannableStringBuilder {
        val line = SpannableStringBuilder()
        val high = SpannableString("!!")
        high.setSpan(
            ForegroundColorSpan(itemView.resources.getColor(R.color.red)),
            0,
            high.length,
            0
        )
        line.bold { append(high) }
        line.append(" ")
        line.append(task.description)
        return line
    }

    private fun isDeadlineOff(task: Task): Boolean {
        return try {
            if (task.date != "") LocalDate.parse(task.date).isBefore(LocalDate.now()) else false
        } catch (e: Exception) {
            false
        }
    }
}
