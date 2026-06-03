package com.example.machines.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.domain.model.MachineSlot
import com.example.machines.ui.component.BookingConfirmationSheet
import com.example.machines.ui.component.MachinesBottomSheet
import com.example.navigation.Screen
import java.time.Instant
import java.time.ZoneId
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.BorderStroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineDetailScreen(
    programId: String,
    navController: NavController
) {
    val viewModel: MachineDetailViewModel = hiltViewModel()
    val slotsGridState = rememberLazyGridState()
    val context = LocalContext.current

    LaunchedEffect(programId) {
        viewModel.loadProgram(programId)
    }
    LaunchedEffect(Unit) {
        viewModel.connectSlotUpdates()
    }
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    val program = viewModel.program ?: return
    var showMachines by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(end = 50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(program.name)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(program.description)
            Spacer(Modifier.height(12.dp))
            Text("Температура: ${program.temperature}°C")
            Text("Обороты: ${program.spinSpeed}")
            Text("Цена: ${program.price} ₽")

            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = viewModel.selectedDate ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Дата") },
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Выбрать дату")
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { showMachines = true },
                enabled = viewModel.selectedDate != null && !viewModel.isLoadingMachines,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    viewModel.selectedMachine?.let { "Машина: ${it.name}" }
                        ?: "Выбрать машину"
                )
            }

            Spacer(Modifier.height(16.dp))

            if (viewModel.isLoadingSlots) {
                CircularProgressIndicator()
            } else if (viewModel.slots.isNotEmpty()) {
                Text("Доступные слоты")

                Spacer(Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    state = slotsGridState,
                    modifier = Modifier.height(220.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = viewModel.slots,
                        key = { slot -> slot.id }
                    ) { slot ->
                        SlotButton(
                            slot = slot,
                            isSelected = viewModel.selectedSlot?.id == slot.id,
                            onClick = {
                                viewModel.selectSlot(slot)
                            }
                        )
                    }
                }
            }
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.bookSelectedSlot() },
                    enabled = viewModel.selectedSlot != null && !viewModel.isBooking,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (viewModel.isBooking) "Бронирование..."
                        else "Забронировать"
                    )
                }
            }
        }
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val millis = datePickerState.selectedDateMillis
                            if (millis != null) {
                                val localDate = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()

                                val formatted = localDate.format(
                                    java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")
                                )

                                viewModel.selectDate(formatted)
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("ОК")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Отмена")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        if (showMachines) {
            MachinesBottomSheet(
                machines = viewModel.machines,
                onApply = { machine ->
                    viewModel.selectMachine(machine)
                    showMachines = false
                },
                onDismiss = { showMachines = false }
            )
        }
        viewModel.bookingFinishUi?.let { ui ->
            BookingConfirmationSheet(
                ui = ui,
                onCancelConfirmed = {
                    viewModel.dismissBookingFinishUi()
                    navController.navigate(Screen.Machines.route) {
                        popUpTo(Screen.Machines.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onPayNow = {
                    val existingBookingId = ui.bookingIdForPayment

                    if (existingBookingId != null) {
                        navController.navigate(Screen.Payment.createRoute(existingBookingId))
                    } else {
                        viewModel.createPaidBookingAndOpenPayment { bookingId ->
                            navController.navigate(Screen.Payment.createRoute(bookingId))
                        }
                    }
                },
                onPayLater = {
                    viewModel.finishFirstBookingAndGoHome {
                        navController.navigate(Screen.Machines.route) {
                            popUpTo(Screen.Machines.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                onPayExisting = {
                    val existingBookingId =
                        ui.bookingIdForPayment ?: return@BookingConfirmationSheet
                    navController.navigate(Screen.Payment.createRoute(existingBookingId))
                }
            )
        }
        DisposableEffect(Unit) {
            onDispose {
                viewModel.disconnectSlotUpdates()
                viewModel.releaseSelectedSlot()
            }
        }
    }

@Composable
fun SlotButton(
    slot: MachineSlot,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Log.d(
        "SlotButton",
        "slot=${slot.id}, isHeld=${slot.isHeld}, heldByMe=${slot.heldByMe}, isBooked=${slot.isBooked}"
    )
    val context = LocalContext.current

    val isLockedByOther = slot.isHeld && !slot.heldByMe
    val isBooked = slot.isBooked

    OutlinedButton(
        onClick = {
            when {
                isLockedByOther -> {
                    Toast.makeText(
                        context,
                        "Этот слот выбрал другой человек",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                isBooked -> {
                    Toast.makeText(
                        context,
                        "Этот слот уже забронирован",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    onClick()
                }
            }
        },
        enabled = true,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.primary
                isLockedByOther -> Color(0xFFFFE0B2)
                isBooked -> Color.LightGray
                else -> Color.Transparent
            },
            contentColor = when {
                isSelected -> Color.White
                isLockedByOther -> Color(0xFFE65100)
                isBooked -> Color.DarkGray
                else -> MaterialTheme.colorScheme.onSurface
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = when {
                isSelected -> MaterialTheme.colorScheme.primary
                isLockedByOther -> Color(0xFFFF9800)
                isBooked -> Color.Gray
                else -> MaterialTheme.colorScheme.outline
            }
        )
    ) {
        Text(
            text = formatTime(slot.startTime)
        )
    }
}


    fun formatTime(dateTime: String): String {
        return try {
            dateTime.substring(11, 16)
        } catch (e: Exception) {
            dateTime
        }
    }
