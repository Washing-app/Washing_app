package com.example.machines.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun FilterChips(
    options: List<Int>,
    selected: Set<Int>,
    onToggle: (Int) -> Unit
) {

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { value ->

            FilterChip(
                selected = value in selected,
                onClick = { onToggle(value) },
                label = { Text(value.toString()) }
            )
        }
    }
}