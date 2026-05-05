package com.example.data.repository

import com.example.data.remote.api.PaymentApi
import com.example.data.remote.dto.toDomain
import com.example.domain.model.PaymentCheckout
import com.example.domain.repository.PaymentRepository
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApi
) : PaymentRepository {

    override suspend fun createPayment(bookingId: String): PaymentCheckout {
        return api.createPayment(bookingId).toDomain()
    }
}