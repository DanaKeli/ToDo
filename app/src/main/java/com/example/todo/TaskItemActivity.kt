package com.example.todo

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.todo.data.database.DBBuilder
import com.example.todo.data.database.TasksDataBase
import com.example.todo.data.database.entity.Task
import com.example.todo.databinding.TaskItemBinding
import kotlinx.coroutines.*
import java.util.*


public class TaskItemActivity : AppCompatActivity() {

    private lateinit var binding: TaskItemBinding
    private lateinit var dialog: AlertDialog

    private var description: String = ""
    private var priority: String = "Нет"
    private var date: String = ""
    private var createdAt: String = ""
    private var updatedAt: String = ""
    private val isDone = false
    lateinit var task: Task
    private var id = taskId
    private var db: TasksDataBase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TaskItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DBBuilder.getInstance(this)
        intent
        if (intent.hasExtra("task")) {
            val task = intent.getParcelableExtra("task") as? Task
            if (task != null) {
                binding.editTextDescription.setText(task.description, TextView.BufferType.EDITABLE)
                binding.textViewPriority.text = task.priority
                binding.textViewDate.text = task.date
                if (task.date != null) {
                    binding.switchDoBefore.isChecked
                }
            }
        }

        binding.textViewPriority.setOnClickListener { showAlert(it) }
        binding.textViewPriorityLabel.setOnClickListener { showAlert(it) }
        binding.imageViewCloseCreate.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.textViewSave.setOnClickListener { save(it) }

        binding.textViewDelete.setOnClickListener { delete(it) }

        binding.switchDoBefore.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) calendar() else switchOff()
        }
    }

    private fun switchOff() {
        binding.textViewDate.text = ""
    }

    private fun calendar() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val view: DatePicker
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, day ->
            binding.textViewDate.text = "$day.${month+1}.$year"}, year, month, day)
        dpd.show()
        dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
            applicationContext.resources.getColor(R.color.blue)
        )
        dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
            applicationContext.resources.getColor(R.color.blue)
        )
    }

    private fun save(view: View) {

        description = binding.editTextDescription.text.toString().trim()
        priority = binding.textViewPriority.text.toString()
        date = binding.textViewDate.text.toString()
        binding.textViewDelete.setTextColor(binding.root.resources.getColor(R.color.red))
        binding.imageViewTrashCan.setColorFilter(
            (applicationContext.resources.getColor(R.color.red)), PorterDuff.Mode.SRC_IN
        )
        task = Task(id, description, priority,isDone, date, createdAt, updatedAt)

    }

    private fun delete(view: View) {
        binding.editTextDescription.text.clear()
        binding.textViewPriority.text = "Нет"
        binding.textViewDate.text = ""
        binding.textViewDelete.setTextColor(binding.root.resources.getColor(R.color.gray_light))
        binding.imageViewTrashCan.setColorFilter(
            (applicationContext.resources.getColor(R.color.gray_light)), PorterDuff.Mode.SRC_IN
        )
        binding.textViewPriority.setTextColor(view.resources.getColor(R.color.gray_light))

    }

    private fun showAlert(view: View) {
        val builder = AlertDialog.Builder(view.context)
        val priority = arrayOf<CharSequence>("Нет", "↓ Низкая", "!! Высокая")
        builder.setItems(priority) { _, position: Int ->
            Toast.makeText(this, priority[position].toString(), Toast.LENGTH_SHORT).show()
            binding.textViewPriority.text = priority[position]
            if (priority[position] == "!! Высокая") {
                binding.textViewPriority.setTextColor(view.resources.getColor(R.color.red))
            } else {
                binding.textViewPriority.setTextColor(view.resources.getColor(R.color.gray_light))
            }
        }
        dialog = builder.create()
        dialog.show()
    }
}
