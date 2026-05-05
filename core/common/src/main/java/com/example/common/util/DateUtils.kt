package com.example.common.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val displayFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val apiFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun formatToDisplay(date: String): String {
    return try {
        LocalDate.parse(date, apiFormatter).format(displayFormatter)
    } catch (e: Exception) {
        date
    }
}

fun formatDateTimeDisplay(dateTime: String): String {
    return try {
        val date = dateTime.substring(0, 10)
        val time = dateTime.substring(11, 16)

        val formattedDate = formatToDisplay(date)

        "$formattedDate $time"
    } catch (e: Exception) {
        dateTime
    }
}