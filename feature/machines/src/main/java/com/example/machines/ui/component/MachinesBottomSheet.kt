package com.example.machines.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domain.model.Machine
import androidx.compose.foundation.lazy.grid.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachinesBottomSheet(
    machines: List<Machine>,
    onApply: (Machine) -> Unit,
    onDismiss: () -> Unit
) {

    var selectedMachine by remember { mutableStateOf<Machine?>(null) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp)) {
            Text("Выбор машины")

            Spacer(Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(machines) { machine ->
                    val isSelected = selectedMachine?.id == machine.id

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                selectedMachine = machine
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = machine.id.toString(),
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    selectedMachine?.let(onApply)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedMachine != null
            ) {
                Text("Применить")
            }
        }
    }
}