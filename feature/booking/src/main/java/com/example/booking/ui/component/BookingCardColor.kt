package com.example.booking.ui.component

import androidx.compose.ui.graphics.Color

fun bookingStatusColor(status: String): Color {
    return when (status) {
        "PAID" -> Color(0xFFB3E5FC)
        "PENDING" -> Color(0xFFFFF59D)
        "CANCELLED" -> Color(0xFFEF9A9A)
        "COMPLETED" -> Color(0xFFA5D6A7)
        else -> Color.LightGray
    }
}