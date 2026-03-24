package com.example.machines.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.domain.model.WashProgram
import com.example.machines.ui.component.MachinesBottomSheet
import java.time.Instant
import java.time.ZoneId
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineDetailScreen(
    programId: String,
    navController: NavController
) {
    val viewModel: MachineDetailViewModel = hiltViewModel()

    LaunchedEffect(programId) {
        viewModel.loadProgram(programId)
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

            Button(
                onClick = { },
                enabled = viewModel.selectedMachine != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Выбрать слот")
            }

            Spacer(Modifier.height(16.dp))

            viewModel.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(8.dp))
            }

            if (viewModel.isLoadingSlots) {
                CircularProgressIndicator()
            } else if (viewModel.slots.isNotEmpty()) {
                Text("Доступные слоты")

                Spacer(Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.slots.filter { !it.isBooked }) { slot ->
                        val isSelected = viewModel.selectedSlot?.id == slot.id

                        OutlinedButton(
                            onClick = { viewModel.selectSlot(slot) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected)
                                    MaterialTheme.colorScheme.primary
                                else Color.Transparent
                            )
                        ) {
                            Text(
                                formatTime(slot.startTime),
                                color = if (isSelected) Color.White else Color.Unspecified
                            )
                        }
                    }
                }
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
                                .toString()

                            viewModel.selectDate(localDate)
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
}

fun formatTime(dateTime: String): String {
    return try {
        dateTime.substring(11, 16)
    } catch (e: Exception) {
        dateTime
    }
}