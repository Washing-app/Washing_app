package com.example.data.remote.dto

import com.example.domain.model.PaymentCheckout

data class CreatePaymentResponseDto(
    val bookingId: String,
    val paymentId: String,
    val status: String,
    val confirmationUrl: String
)

fun CreatePaymentResponseDto.toDomain() = PaymentCheckout(
    bookingId = bookingId,
    paymentId = paymentId,
    status = status,
    confirmationUrl = confirmationUrl
)