package com.example.domain.model

data class PrepareBookingResult(
    val paymentRequired: Boolean,
    val blockedByUnpaidBooking: Boolean,
    val existingUnpaidBookingId: String?,
    val slotId: Long,
    val washTypeId: Long,
    val price: Int
)