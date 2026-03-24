package com.example.data.remote.dto

data class MachineSlotDto(
    val id: Long,
    val startTime: String,
    val endTime: String,
    val isBooked: Boolean
)
