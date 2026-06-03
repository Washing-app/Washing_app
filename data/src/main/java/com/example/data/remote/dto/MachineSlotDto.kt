package com.example.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MachineSlotDto(
    val id: Long,
    val startTime: String,
    val endTime: String,

    @SerializedName("booked")
    val isBooked: Boolean,

    @SerializedName("held")
    val isHeld: Boolean,

    val heldByMe: Boolean
)
