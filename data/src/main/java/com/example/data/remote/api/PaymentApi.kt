package com.example.data.remote.api

import com.example.data.remote.dto.CreatePaymentResponseDto
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentApi {

    @POST("payments/bookings/{bookingId}/checkout")
    suspend fun createPayment(
        @Path("bookingId") bookingId: String
    ): CreatePaymentResponseDto
}