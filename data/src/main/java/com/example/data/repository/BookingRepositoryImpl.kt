package com.example.data.repository

import com.example.data.remote.api.BookingApi
import com.example.data.remote.dto.toDomain
import com.example.domain.model.BookingItem
import com.example.domain.repository.BookingRepository
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val api: BookingApi
) : BookingRepository {

    override suspend fun getMyBookings(): List<BookingItem> {
        return api.getMyBookings().map { it.toDomain() }
    }

    override suspend fun getBookingById(id: String): BookingItem {
        return api.getBookingById(id).toDomain()
    }
}