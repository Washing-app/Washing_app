package com.example.booking.ui.component

import androidx.compose.ui.graphics.Color

data class BookingStatusUi(
    val chipBackground: Color,
    val chipText: Color,
    val label: String
)

fun bookingStatusUi(status: String): BookingStatusUi {
    return when (status) {
        "PAID" -> BookingStatusUi(
            chipBackground = Color(0xFFE3F2FD),
            chipText = Color(0xFF1565C0),
            label = "Подтверждена"
        )
        "PENDING_PAYMENT" -> BookingStatusUi(
            chipBackground = Color(0xFFFFF3E0),
            chipText = Color(0xFFEF6C00),
            label = "Ожидает оплаты"
        )
        "CANCELLED" -> BookingStatusUi(
            chipBackground = Color(0xFFFFEBEE),
            chipText = Color(0xFFC62828),
            label = "Отменена"
        )
        "COMPLETED" -> BookingStatusUi(
            chipBackground = Color(0xFFE8F5E9),
            chipText = Color(0xFF2E7D32),
            label = "Завершена"
        )
        else -> BookingStatusUi(
            chipBackground = Color(0xFFE0E0E0),
            chipText = Color(0xFF616161),
            label = status
        )
    }
}