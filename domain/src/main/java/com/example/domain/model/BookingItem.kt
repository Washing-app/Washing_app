package com.example.domain.model

data class BookingItem(
    val id: String,
    val status: String,
    val startTime: String,
    val endTime: String,
    val cancelledAt: String?,
    val machineName: String,
    val washTypeId: Long,
    val washTypeName: String,
    val durationMinutes: Int,
    val price: Int,
    val temperature: Int,
    val spinSpeed: Int
)