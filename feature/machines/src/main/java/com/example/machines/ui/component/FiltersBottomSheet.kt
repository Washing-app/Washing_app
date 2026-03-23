package com.example.machines.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.machines.model.FilterState
import com.example.machines.ui.toggle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    initialState: FilterState,
    onApply: (FilterState) -> Unit,
    onDismiss: () -> Unit
) {

    val sheetState = rememberModalBottomSheetState()

    var temps by remember { mutableStateOf(initialState.temperatures) }
    var speeds by remember { mutableStateOf(initialState.speeds) }
    var minText by remember { mutableStateOf(initialState.minDuration.toString()) }
    var maxText by remember { mutableStateOf(initialState.maxDuration.toString()) }
    var minDuration by remember { mutableStateOf(initialState.minDuration) }
    var maxDuration by remember { mutableStateOf(initialState.maxDuration) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {

        Column(Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Фильтры", style = MaterialTheme.typography.titleLarge)

                TextButton(onClick = onDismiss) {
                    Text("Отмена")
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Температура")

            FilterChips(
                options = listOf(0, 30, 40, 60),
                selected = temps,
                onToggle = {
                    temps = temps.toggle(it)
                }
            )

            Spacer(Modifier.height(16.dp))

            Text("Скорость")

            FilterChips(
                options = listOf(600, 800, 1200),
                selected = speeds,
                onToggle = {
                    speeds = speeds.toggle(it)
                }
            )

            Spacer(Modifier.height(16.dp))

            Text("Длительность")

            Spacer(Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {

                TextField(
                    value = minText,
                    onValueChange = { minText = it },
                    prefix = { Text("от ") },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorResource(R.color.input_background),
                        unfocusedContainerColor = colorResource(R.color.input_background),
                        disabledContainerColor = colorResource(R.color.input_background),

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused) {
                                val value = minText.toIntOrNull()

                                minDuration = when {
                                    value == null -> 15
                                    value < 15 -> 15
                                    else -> value
                                }

                                if (minDuration > maxDuration) {
                                    maxDuration = minDuration
                                    maxText = maxDuration.toString()
                                }

                                minText = minDuration.toString()
                            }
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true
                )

                TextField(
                    value = maxText,
                    onValueChange = { maxText = it },
                    prefix = { Text("до ") },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorResource(R.color.input_background),
                        unfocusedContainerColor = colorResource(R.color.input_background),
                        disabledContainerColor = colorResource(R.color.input_background),

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .weight(1f)

                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused) {
                                val value = maxText.toIntOrNull()

                                maxDuration = when {
                                    value == null -> minDuration
                                    value > 165 -> 165
                                    else -> value
                                }

                                if (maxDuration < minDuration) {
                                    maxDuration = minDuration
                                }

                                maxText = maxDuration.toString()
                            }
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true
                )
            }
            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                AssistChip(
                    onClick = {
                        minDuration = 15
                        maxDuration = 30

                        minText = minDuration.toString()
                        maxText = maxDuration.toString()
                    },
                    label = { Text("<30 мин.") }
                )

                AssistChip(
                    onClick = {
                        minDuration = 30
                        maxDuration = 60

                        minText = minDuration.toString()
                        maxText = maxDuration.toString()
                    },
                    label = { Text("30–60 мин.") }
                )

                AssistChip(
                    onClick = {
                        minDuration = 60
                        maxDuration = 165

                        minText = minDuration.toString()
                        maxText = maxDuration.toString()
                    },
                    label = { Text("60+ мин.") }
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    onApply(
                        FilterState(
                            temperatures = temps,
                            speeds = speeds,
                            minDuration = minDuration,
                            maxDuration = maxDuration
                        )
                    )
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Применить")
            }
        }
    }
}