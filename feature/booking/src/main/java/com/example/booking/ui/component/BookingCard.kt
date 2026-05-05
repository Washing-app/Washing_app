package com.example.booking.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.common.util.formatDateTimeDisplay
import com.example.domain.model.BookingItem

@Composable
fun BookingCard(
    booking: BookingItem,
    faded: Boolean,
    onClick: (BookingItem) -> Unit
) {
    val statusUi = bookingStatusUi(booking.status)
    val containerColor = if (faded) Color(0xFFF7F7F7) else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(booking) },
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = booking.machineName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = statusUi.label,
                    color = statusUi.chipText,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(statusUi.chipBackground)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }

            Text(
                text = "${formatDateTimeDisplay(booking.startTime)} - ${formatTime(booking.endTime)}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Тип стирки: ${booking.washTypeName}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Цена: ${booking.price} ₽",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun formatTime(dateTime: String): String {
    return try {
        dateTime.substring(11, 16)
    } catch (e: Exception) {
        dateTime
    }
}