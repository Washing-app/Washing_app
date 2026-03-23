package com.example.machines.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineDetailScreen(
    programId: String,
    navController: NavController
) {
    val viewModel: MachineDetailViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
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
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(program.description)

            Spacer(Modifier.height(12.dp))

            Text("Температура: ${program.temperature}°C")
            Text("Обороты: ${program.spinSpeed}")
            Text("Цена: ${program.price} ₽")

            Spacer(Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = viewModel.selectedDate ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Дата") },
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, null)
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { showMachines = true },
                enabled = viewModel.selectedDate != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Выбрать машину")
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {},
                enabled = viewModel.selectedMachine != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Выбрать слот")
            }
        }
    }
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    viewModel.selectDate("выбрана дата")
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = rememberDatePickerState())
        }
    }
    if (showMachines) {
        MachinesBottomSheet(
            onApply = {
                viewModel.selectMachine(it)
                showMachines = false
            },
            onDismiss = { showMachines = false }
        )
    }
}