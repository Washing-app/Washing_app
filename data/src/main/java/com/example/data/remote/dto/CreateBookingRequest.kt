package com.example.data.remote.dto

data class CreateBookingRequest(
    val userId: String,
    val slotId: Long,
    val washTypeId: Long
)
