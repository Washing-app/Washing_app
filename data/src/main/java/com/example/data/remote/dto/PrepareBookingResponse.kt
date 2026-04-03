package com.example.data.remote.dto

import com.example.domain.model.PrepareBookingResult

data class PrepareBookingResponse(
    val paymentRequired: Boolean,
    val blockedByUnpaidBooking: Boolean,
    val existingUnpaidBookingId: String?,
    val slotId: Long,
    val washTypeId: Long,
    val price: Int
)

fun PrepareBookingResponse.toDomain() = PrepareBookingResult(
    paymentRequired = paymentRequired,
    blockedByUnpaidBooking = blockedByUnpaidBooking,
    existingUnpaidBookingId = existingUnpaidBookingId,
    slotId = slotId,
    washTypeId = washTypeId,
    price = price
)