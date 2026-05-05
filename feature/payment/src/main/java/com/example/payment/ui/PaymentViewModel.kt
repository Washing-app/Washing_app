package com.example.payment.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.PaymentCheckout
import com.example.domain.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: PaymentRepository
) : ViewModel() {

    var isLoading = false
        private set

    var checkout by mutableStateOf<PaymentCheckout?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun createPayment(bookingId: String) {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                checkout = repository.createPayment(bookingId)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Не удалось создать платеж"
            } finally {
                isLoading = false
            }
        }
    }
}