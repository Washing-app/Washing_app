package com.example.domain.repository

import com.example.domain.model.BookingItem

interface BookingRepository {
    suspend fun getMyBookings(): List<BookingItem>
    suspend fun getBookingById(id: String): BookingItem
}