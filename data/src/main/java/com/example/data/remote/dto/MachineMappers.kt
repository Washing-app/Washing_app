package com.example.data.remote.dto

import com.example.domain.model.Machine
import com.example.domain.model.MachineSlot

fun MachineDto.toDomain() = Machine(
    id = id,
    name = name,
    status = status,
    location = location
)

fun MachineSlotDto.toDomain() = MachineSlot(
    id = id,
    startTime = startTime,
    endTime = endTime,
    isBooked = isBooked
)