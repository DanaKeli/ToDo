package com.example.todo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.LocalTime
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = NotesAdapter()
    private val noteList = arrayListOf<Note>()
    private var isDOneVisible = false
    private var tasksAmount: Int? = null
    lateinit var manager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        noteList.add(Note("Some to do", LocalDate.of(2021, 5, 5), "high", false))
        noteList.add(Note("Some to do", null, "no", false))
        noteList.add(Note("Some to do", LocalDate.of(2021, 7, 1), "low", false))
        noteList.add(Note("Some to do", LocalDate.of(2021, 8, 4), "low", false))
        noteList.add(Note("Some to do", LocalDate.of(2021, 12, 4), "no", false))
        noteList.add(Note("Some to do", LocalDate.of(2021, 7, 1), "high", false))
        noteList.add(Note("Some to do", LocalDate.of(2021, 1, 4), "low", false))
        noteList.add(Note("Some to do", LocalDate.of(2021, 10, 4), "high", false))



        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.adapter = adapter
        adapter.noteInfoList =
            ArrayList<Note>(noteList.filterNot { it.isDone }.sortedBy { note -> note.priority }
                .sortedBy { note -> note.date })
        binding.tvDone.text = "Выполнено - ${noteList.count { it.isDone }}"

        val obj = object : NotesAdapter.OnNoteClickListener {
            override fun onNoteClick(position: Int) {
                val intent = Intent(applicationContext, NoteItemActivity::class.java)
                intent.putExtra("note", noteList[position] as Parcelable)
                startActivity(intent)
            }
        }
        adapter.setOnNoteClickListener(obj)
        leftSwipe()
        rightSwipe()

        tasksAmount = noteList.filterNot { it.isDone }.count { it.date == LocalDate.now() }
        if (tasksAmount != 0 && LocalTime.now() >= LocalTime.of(8, 0)) {
            createNotificationChannel(
                NOTIFICATION_ID.toString(),
                "ToDo",
                "На сегодня есть задачи: $tasksAmount шт. Не забудьте"

            )
            sendNotification()
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
                adapter.noteInfoList.removeAt(viewHolder.adapterPosition)
                adapter.notifyDataSetChanged()
            }
//            override fun onChildDrow(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
//                                     dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
//
//            }
//            )
        }
        val leftHelper = ItemTouchHelper(leftCallback)
        leftHelper.attachToRecyclerView(binding.rvNotes)
    }

    private fun rightSwipe() {
        val leftCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.noteInfoList[viewHolder.adapterPosition].isDone = true
                adapter.notifyDataSetChanged()
            }
//            override fun onChildDrow(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
//                                     dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
//
//            }
//            )
        }
        val leftHelper = ItemTouchHelper(leftCallback)
        leftHelper.attachToRecyclerView(binding.rvNotes)
    }

    private fun createNotificationChannel(id: String, name: String, description: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)
        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.BLUE
        manager.createNotificationChannel(channel)
    }

    private fun sendNotification() {
        val resultIntent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("ToDo")
            .setContentText("На сегодня есть задачи: $tasksAmount шт. Не забудьте")
            .setSmallIcon(android.R.drawable.checkbox_on_background)
            .setChannelId(CHANNEL_ID)
            .setAutoCancel(false)
            .setContentIntent(pendingIntent)
            .build()
        manager.notify(NOTIFICATION_ID, notification)
    }

    fun onClickAdd(view: View) {
        val intent = Intent(this, NoteItemActivity::class.java)
        startActivity(intent)
    }

    fun onClickDoneVisibility(view: View) {
        if (isDOneVisible) {
            binding.ivDoneVisibility.setColorFilter(
                applicationContext.resources.getColor(R.color.gray_light),
                PorterDuff.Mode.SRC_IN
            )
            adapter.noteInfoList =
                ArrayList<Note>((noteList.filterNot { it.isDone }.sortedBy { note -> note.priority }
                    .sortedBy { note -> note.date }))
            isDOneVisible = false
            adapter.notifyDataSetChanged()
        } else {
            binding.ivDoneVisibility.setColorFilter(
                applicationContext.resources.getColor(R.color.blue),
                PorterDuff.Mode.SRC_IN
            )
            adapter.noteInfoList = ArrayList<Note>(noteList.sortedBy { note -> note.priority }
                .sortedBy { note -> note.date })
            isDOneVisible = true
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "com.example.todo"
    }
}