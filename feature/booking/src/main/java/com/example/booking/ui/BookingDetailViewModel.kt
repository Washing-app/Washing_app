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
import javax.inject.Inject

@HiltViewModel
class BookingDetailViewModel @Inject constructor(
    private val repository: BookingRepository
) : ViewModel() {

    var booking by mutableStateOf<BookingItem?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadBooking(id: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                booking = repository.getBookingById(id)
            } finally {
                isLoading = false
            }
        }
    }
}