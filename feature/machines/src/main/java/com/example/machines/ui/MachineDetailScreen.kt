package com.example.machines.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.WashProgram

@Composable
fun MachineDetailScreen(program: WashProgram) {

    Column(Modifier.padding(16.dp)) {

        Text(program.name, style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(8.dp))

        Text("Температура: ${program.temperature}°C")
        Text("Скорость: ${program.spinSpeed}")
        Text("Время: ${program.durationMinutes} мин")
        Text("Цена: ${program.price} ₽")

        Spacer(Modifier.height(12.dp))

        Text(program.description)

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Выбрать дату")
        }
    }
}