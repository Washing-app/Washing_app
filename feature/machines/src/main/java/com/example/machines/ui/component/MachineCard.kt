package com.example.machines.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.WashProgram

@Composable
fun MachineCard(
    program: WashProgram,
    onClick: (WashProgram) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(program) }
    ) {
        Column(Modifier.padding(12.dp)) {

            Text(program.name, style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))

            Text("${program.temperature}°C")
            Text("${program.durationMinutes} мин")
            Text("${program.price} ₽")
        }
    }
}