package com.example.booking.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.BookingItem
import com.example.domain.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val repository: BookingRepository
) : ViewModel() {

    var allBookings by mutableStateOf<List<BookingItem>>(emptyList())
        private set

    var currentBookings by mutableStateOf<List<BookingItem>>(emptyList())
        private set

    var pastBookings by mutableStateOf<List<BookingItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadBookings() {
        viewModelScope.launch {
            isLoading = true
            try {
                val bookings = repository.getMyBookings()
                    .sortedBy { java.time.LocalDateTime.parse(it.startTime) }

                allBookings = bookings

                val now = java.time.LocalDateTime.now()

                currentBookings = bookings.filter {
                    java.time.LocalDateTime.parse(it.endTime).isAfter(now)
                }

                pastBookings = bookings.filter {
                    !java.time.LocalDateTime.parse(it.endTime).isAfter(now)
                }
            } finally {
                isLoading = false
            }
        }
    }
}