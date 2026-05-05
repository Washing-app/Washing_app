package com.example.domain.repository

import com.example.domain.model.PaymentCheckout

interface PaymentRepository {
    suspend fun createPayment(bookingId: String): PaymentCheckout
}