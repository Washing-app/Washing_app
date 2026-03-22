package com.example.machines.ui

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.domain.model.WashProgram
import com.example.domain.repository.MachinesRepository
import com.example.machines.model.FilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachinesViewModel @Inject constructor(
    private val repository: MachinesRepository
) : ViewModel() {

    var programs by mutableStateOf<List<WashProgram>>(emptyList())
        private set

    init {
        loadPrograms()
    }

    private fun loadPrograms() {
        viewModelScope.launch {
            try {
                programs = repository.getPrograms()
            } catch (e: Exception) {
                println("ERROR: ${e.message}")
            }
        }
    }

    var filters by mutableStateOf(FilterState())
        private set

    fun toggleTemperature(value: Int) {
        filters = filters.copy(
            temperatures = filters.temperatures.toggle(value)
        )
    }

    fun toggleSpeed(value: Int) {
        filters = filters.copy(
            speeds = filters.speeds.toggle(value)
        )
    }

    fun applyFilters(newFilters: FilterState) {
        filters = newFilters
    }
    fun setMinDuration(value: Int) {
        val newMin = value.coerceAtLeast(15)

        filters = filters.copy(
            minDuration = newMin,
            maxDuration = maxOf(newMin, filters.maxDuration)
        )
    }

    fun setMaxDuration(value: Int) {
        val newMax = value.coerceAtMost(165)

        filters = filters.copy(
            maxDuration = newMax,
            minDuration = minOf(filters.minDuration, newMax)
        )
    }

    fun setPreset(min: Int, max: Int) {
        filters = filters.copy(
            minDuration = min,
            maxDuration = max
        )
    }
}