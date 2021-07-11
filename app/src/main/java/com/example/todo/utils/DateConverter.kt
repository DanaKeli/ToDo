package com.example.todo.utils

import java.sql.Timestamp
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateConverter {

    fun String.toTimestamp(str: String): Timestamp {
        val date = LocalDate.parse(str, DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss"))
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond as Timestamp
    }
}