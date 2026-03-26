package com.example.booking.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domain.model.BookingItem

@Composable
fun BookingCard(
    booking: BookingItem,
    faded: Boolean,
    onClick: (BookingItem) -> Unit
) {
    val baseColor = bookingStatusColor(booking.status)
    val containerColor = if (faded) baseColor.copy(alpha = 0.55f) else baseColor

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(booking) },
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${formatDateTime(booking.startTime)} - ${formatTime(booking.endTime)}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Цена: ${booking.price} ₽",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun formatDateTime(dateTime: String): String {
    return try {
        val date = dateTime.substring(0, 10)
        val time = dateTime.substring(11, 16)
        "$date $time"
    } catch (e: Exception) {
        dateTime
    }
}

private fun formatTime(dateTime: String): String {
    return try {
        dateTime.substring(11, 16)
    } catch (e: Exception) {
        dateTime
    }
}