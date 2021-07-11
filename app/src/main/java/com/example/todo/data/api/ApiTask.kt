package com.example.todo.data.api

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class ApiTask(
    @SerializedName("id")
    val _id: Int,

    @SerializedName("text")
    val description: String ,

    @SerializedName("importance")
    val priority: String,

    @SerializedName("done")
    val isDone: Boolean,

    @SerializedName("deadline")
    val date: Timestamp,

    @SerializedName("created_at")
    val createdAt: Timestamp,

    @SerializedName("updated_at")
    val updatedAt: Timestamp
)