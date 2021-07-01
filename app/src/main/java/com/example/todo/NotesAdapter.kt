package com.example.todo

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ItemNoteInfoBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

class NotesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onNoteClickListener: OnNoteClickListener? = null
    private var onCheckBoxClickListener: OnCheckBoxClickListener? = null

    interface OnNoteClickListener {
        fun onNoteClick(position: Int)
    }

    interface OnCheckBoxClickListener {
        fun onCheckBoxClick(position: Int)
    }

    fun setOnNoteClickListener(onNoteClickListener: OnNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener
    }

    var noteInfoList = ArrayList<Note>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class NoteViewHolder(private val binding: ItemNoteInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {

            if (note.isDone) {
                binding.imageViewCheckBox.setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_checked)
                )
                strike(note)
            } else {
                redOrPlain(note)
                viewOfPriority(note)
            }

            binding.textViewDateItem.text = note.date.toString()
            if (note.date == null) {
                binding.textViewDateItem.text = ""
            }

            binding.imageViewInfo.setOnClickListener {
                onNoteClickListener?.onNoteClick(adapterPosition)
            }

            binding.imageViewCheckBox.setOnClickListener {
                onCheckBoxClickListener?.onCheckBoxClick(adapterPosition)
                if (note.isDone) {
                    redOrPlain(note)
                    note.isDone = false
                    viewOfPriority(note)
                    binding.textViewDescriptionMain.paintFlags = 0
                    binding.textViewDescriptionMain.setTextColor(Color.GRAY)
                } else {
                    binding.imageViewCheckBox.setImageDrawable(
                        ContextCompat.getDrawable(itemView.context, R.drawable.ic_checked)
                    )
                    note.isDone = true
                    strike(note)
                }
            }
        }

        private fun viewOfPriority(note: Note) {
            when (note.priority) {
                "high" -> binding.textViewDescriptionMain.setText(
                    exclamation(note), TextView.BufferType.SPANNABLE
                )
                "low" -> binding.textViewDescriptionMain.setText(
                    arrow(note), TextView.BufferType.SPANNABLE
                )
                else -> plain(note)
            }
        }

        private fun redOrPlain(note: Note) {
            if (isDeadlineOff(note)) {
                binding.imageViewCheckBox.setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, R.drawable.unchecked_red)
                )
            } else {
                binding.imageViewCheckBox.setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, R.drawable.unchecked)
                )
            }
        }

        private fun plain(note: Note) {
            binding.textViewDescriptionMain.text = note.description
            binding.textViewDescriptionMain.setTextColor(Color.GRAY)
            binding.textViewDescriptionMain.paintFlags = 0
        }

        private fun strike(note: Note) {
            binding.textViewDescriptionMain.text = note.description
            binding.textViewDescriptionMain.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.textViewDescriptionMain.setTextColor(itemView.resources.getColor(R.color.gray_light))
        }

        private fun arrow(note: Note): SpannableStringBuilder {
            val line = SpannableStringBuilder()
            val low = SpannableString("↓")
            low.setSpan(
                ForegroundColorSpan(Color.GRAY),
                0, low.length, 0
            )
            line.bold { append(low) }
            line.append(" ")
            line.append(note.description)
            return line
        }

        private fun exclamation(note: Note): SpannableStringBuilder {
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
            line.append(note.description)
            return line
        }

        private fun isDeadlineOff(note: Note): Boolean {
            return try {
                if (note.date != null) note.date?.isBefore(LocalDate.now()) == true else false
            } catch (e: Exception) {
                false
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteInfoBinding.inflate(layoutInflater, parent, false)
        return NoteViewHolder(binding)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val note: Note = noteInfoList[position]
        val noteViewHolder = holder as NoteViewHolder
        noteViewHolder.bind(note)

        holder.itemView.setOnClickListener {
            View.OnClickListener() {
                @Override
                fun onClick(view: View) {
                    onNoteClickListener?.onNoteClick(position)
                }
            }
        }
    }

    override fun getItemCount(): Int = noteInfoList.size


}
