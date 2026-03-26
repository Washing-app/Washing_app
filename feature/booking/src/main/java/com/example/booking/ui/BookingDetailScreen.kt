package com.example.booking.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailScreen(
    bookingId: String,
    navController: NavController,
    viewModel: BookingDetailViewModel
) {
    LaunchedEffect(bookingId) {
        viewModel.loadBooking(bookingId)
    }

    val booking = viewModel.booking

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Запись")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->

        if (viewModel.isLoading || booking == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Тип стирки: ${booking.washTypeName}")
            Spacer(Modifier.height(8.dp))
            Text("Время: ${booking.durationMinutes} мин")
            Text("Температура: ${booking.temperature}°C")
            Text("Цена: ${booking.price} ₽")
            Text("Дата и время начала: ${formatDateTime(booking.startTime)}")
            Text("Дата и время окончания: ${formatDateTime(booking.endTime)}")
            Text("Машинка: ${booking.machineName}")
        }
    }
}

private fun formatDateTime(dateTime: String): String {
    return try {
        val date = dateTime.substring(0, 10)
        val time = dateTime.substring(11, 16)
        "$date $time"
    } catch (e: Exception) {
        dateTime
    }
}