package com.example.todo


data class Note (
    var description: String? = null,
    var date: String? = null,
    var priority: String? = null,
    var isDone: Boolean = false
)