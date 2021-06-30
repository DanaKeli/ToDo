package com.example.todo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Note (
    var description: String? = null,
    var date: String? = null,
    var priority: String? = null,
    var isDone: Boolean = false
): Parcelable