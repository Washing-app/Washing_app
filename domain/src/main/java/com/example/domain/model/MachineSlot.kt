package com.example.domain.model

data class MachineSlot(
    val id: Long,
    val startTime: String,
    val endTime: String,
    val isBooked: Boolean,
    val isHeld: Boolean,
    val heldByMe: Boolean
)
