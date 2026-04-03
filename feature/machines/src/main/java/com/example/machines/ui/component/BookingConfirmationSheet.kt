package com.example.machines.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.machines.ui.BookingConfirmationUi
import com.example.machines.ui.BookingFinishMode
import com.example.machines.ui.BookingFinishUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingConfirmationSheet(
    ui: BookingFinishUi,
    onCloseToMain: () -> Unit,
    onPayNow: () -> Unit,
    onPayLater: () -> Unit,
    onPayExisting: () -> Unit,
    onReturn: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Запись")
                    }
                },
                actions = {
                    IconButton(onClick = onCloseToMain) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть")
                    }
                }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Дата: ${ui.date}")
                Text("Время: ${ui.time}")
                Text("Стиральная машина: ${ui.machineName}")
                Text("Тип стирки: ${ui.washTypeName}")
                Text("Стоимость: ${ui.price} ₽")
                Text("Не забудьте оплатить свою стирку")
            }

            when (ui.mode) {
                BookingFinishMode.FREE_SUCCESS -> {
                    Button(
                        onClick = onReturn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Вернуться")
                    }
                }

                BookingFinishMode.PAY_CHOICE -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onPayNow,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Оплатить сразу")
                        }

                        OutlinedButton(
                            onClick = onPayLater,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Оплатить позже")
                        }
                    }
                }

                BookingFinishMode.BLOCKED_BY_UNPAID -> {
                    Button(
                        onClick = onPayExisting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Оплатить")
                    }
                }
            }
        }
    }
}