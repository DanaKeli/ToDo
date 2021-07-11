package com.example.todo.data.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tasks")
@Parcelize
data class Task (
    @PrimaryKey(autoGenerate = true)
    val _id: Int,
    var description: String,
    var priority: String,
    var isDone: Boolean,
    var date: String,
    var createdAt: String,
    var updatedAt: String
): Parcelable