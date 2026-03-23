package com.example.machines.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.domain.model.WashProgram
import com.example.machines.ui.component.FiltersBottomSheet
import com.example.machines.ui.component.MachineCard
import com.example.ui.R

@Composable
fun MachinesScreen(
    onItemClick: (WashProgram) -> Unit
) {
    val viewModel: MachinesViewModel = hiltViewModel()

    var showFilters by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = colorResource(id = R.color.background),
        topBar = {
            SearchTopBar(
                onFilterClick = { showFilters = true }
            )
        }
    ) { padding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 8.dp,
                end = 8.dp,
                bottom = 8.dp,
                top = padding.calculateTopPadding() + 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.programs) { program ->
                MachineCard(program, onItemClick)
            }
        }
    }

    if (showFilters) {
        FiltersBottomSheet(
            initialState = viewModel.filters,
            onApply = { viewModel.applyFilters(it) },
            onDismiss = { showFilters = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    onFilterClick: () -> Unit
) {

    var query by remember { mutableStateOf("") }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Поиск...") },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorResource(R.color.input_background),
                        unfocusedContainerColor = colorResource(R.color.input_background),
                        disabledContainerColor = colorResource(R.color.input_background),

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )

                Spacer(Modifier.width(8.dp))

                IconButton(onClick = onFilterClick) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filters")
                }
            }
        }
    )
}


fun <T> Set<T>.toggle(item: T): Set<T> {
    return if (contains(item)) minus(item) else plus(item)
}