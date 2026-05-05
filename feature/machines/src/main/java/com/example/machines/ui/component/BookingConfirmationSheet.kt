package com.example.machines.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.machines.ui.BookingFinishMode
import com.example.machines.ui.BookingFinishUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingConfirmationSheet(
    ui: BookingFinishUi,
    onCancelConfirmed: () -> Unit,
    onPayNow: () -> Unit,
    onPayLater: () -> Unit,
    onPayExisting: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )

    var showCancelDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text("Отмена брони")
            },
            text = {
                Text(
                    "Если не произведёте оплату сейчас, то бронь не будет подтверждена. Вы точно уверены, что хотите отменить бронь?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCancelDialog = false
                        onCancelConfirmed()
                    }
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCancelDialog = false
                    }
                ) {
                    Text("Нет")
                }
            }
        )
    }

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        dragHandle = null,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false
        )
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
                    IconButton(onClick = { showCancelDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть"
                        )
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
                BookingFinishMode.FIRST_BOOKING -> {
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

                BookingFinishMode.REQUIRE_PAYMENT -> {
                    Button(
                        onClick = onPayNow,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Оплатить")
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