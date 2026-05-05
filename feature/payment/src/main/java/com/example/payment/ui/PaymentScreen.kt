package com.example.payment.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.core.net.toUri

@Composable
fun PaymentScreen(
    bookingId: String,
    onBackToMachines: () -> Unit,
    viewModel: PaymentViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val checkout by androidx.compose.runtime.remember { androidx.compose.runtime.derivedStateOf { viewModel.checkout } }
    val error by androidx.compose.runtime.remember { androidx.compose.runtime.derivedStateOf { viewModel.errorMessage } }

    LaunchedEffect(bookingId) {
        android.util.Log.d("PAYMENT_SCREEN", "bookingId = $bookingId")
        viewModel.createPayment(bookingId)
    }

    LaunchedEffect(checkout?.confirmationUrl) {
        val url = checkout?.confirmationUrl ?: return@LaunchedEffect
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Оплата бронирования")

        when {
            viewModel.isLoading -> {
                CircularProgressIndicator()
                Text("Создаём ссылку на оплату...")
            }

            checkout != null -> {
                Text("Платёж создан")
                Text("Статус: ${checkout?.status}")
                Button(
                    onClick = {
                        val url = checkout?.confirmationUrl ?: return@Button
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    }
                ) {
                    Text("Открыть оплату ещё раз")
                }

                Button(onClick = onBackToMachines) {
                    Text("Вернуться на главный экран")
                }
            }

            error != null -> {
                Text(error ?: "Ошибка оплаты")
                Button(
                    onClick = { viewModel.createPayment(bookingId) }
                ) {
                    Text("Повторить")
                }
            }
        }
    }
}