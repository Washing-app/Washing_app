package com.example.domain.model

data class PaymentCheckout(
    val bookingId: String,
    val paymentId: String,
    val status: String,
    val confirmationUrl: String
)
