package com.example.todo

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.databinding.ActivityMainBinding
import java.time.LocalDate

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val adapter = NotesAdapter()
    private val noteList = arrayListOf<Note>()
    private var isDOneVisible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        noteList.add(Note("Some to do", "03.02.2021", "high", false))
        noteList.add(Note("Some to do", "", "no", false))
        noteList.add(Note("Some to do", "02.01.2021", "low", true))
        noteList.add(Note("Some to do", "02.06.2021", "low", false))
        noteList.add(Note("Some to do", "02.04.2021", "no", false))
        noteList.add(Note("Some to do", "02.12.2021", "high", false))
        noteList.add(Note("Some to do", "02.11.2021", "low", false))
        noteList.add(Note("Some to do", "02.07.2021", "high", false))




        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.adapter = adapter
        adapter.noteInfoList = ArrayList<Note>(noteList.filterNot { it.isDone }.sortedBy { note -> note.priority }.sortedBy { note -> note.date })
        binding.tvDone.text = "Выполнено - ${noteList.count { it.isDone }}"

        val obj = object : NotesAdapter.OnNoteClickListener {
            override fun onNoteClick(position: Int) {
                val intent = Intent(applicationContext, NoteItemActivity::class.java)
                intent.putExtra("note", noteList[position] as Parcelable)
                startActivity(intent)
            }
        }
        adapter.setOnNoteClickListener(obj)
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
            adapter.noteInfoList = ArrayList<Note>((noteList.filterNot { it.isDone }.sortedBy { note -> note.priority }.sortedBy { note -> note.date }))
            isDOneVisible = false
            adapter.notifyDataSetChanged()
        } else {
            binding.ivDoneVisibility.setColorFilter(
                applicationContext.resources.getColor(R.color.blue),
                PorterDuff.Mode.SRC_IN
            )
            adapter.noteInfoList = ArrayList<Note>(noteList.sortedBy { note -> note.priority }.sortedBy { note -> note.date })
            isDOneVisible = true
            adapter.notifyDataSetChanged()
        }
    }
}