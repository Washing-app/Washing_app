package com.example.data.remote.api

import com.example.data.remote.dto.BookingItemDto
import retrofit2.http.GET
import retrofit2.http.Path

interface BookingApi {

    @GET("bookings/my")
    suspend fun getMyBookings(): List<BookingItemDto>

    @GET("bookings/{id}")
    suspend fun getBookingById(
        @Path("id") id: String
    ): BookingItemDto
}