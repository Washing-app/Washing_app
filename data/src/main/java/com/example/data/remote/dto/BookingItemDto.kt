package com.example.data.remote.dto

data class BookingItemDto(
    val id: String,
    val status: String,
    val startTime: String,
    val endTime: String,
    val cancelledAt: String?,
    val machineName: String,
    val washType: WashTypeShortDto
)

data class WashTypeShortDto(
    val id: Long,
    val name: String,
    val durationMinutes: Int,
    val price: Int,
    val temperature: Int,
    val spinSpeed: Int
)
