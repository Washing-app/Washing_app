package com.example.data.remote.dto

import com.example.domain.model.BookingItem

fun BookingItemDto.toDomain() = BookingItem(
    id = id,
    status = status,
    startTime = startTime,
    endTime = endTime,
    cancelledAt = cancelledAt,
    machineName = machineName,
    washTypeId = washType.id,
    washTypeName = washType.name,
    durationMinutes = washType.durationMinutes,
    price = washType.price,
    temperature = washType.temperature,
    spinSpeed = washType.spinSpeed
)