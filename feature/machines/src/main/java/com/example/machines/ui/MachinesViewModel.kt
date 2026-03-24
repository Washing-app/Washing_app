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

    var filteredPrograms by mutableStateOf<List<WashProgram>>(emptyList())
        private set

    var filters by mutableStateOf(FilterState())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadPrograms()
    }

    private fun loadPrograms() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val loadedPrograms = repository.getPrograms()
                programs = loadedPrograms
                filteredPrograms = applyFiltersToList(loadedPrograms, filters)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Не удалось загрузить программы"
            } finally {
                isLoading = false
            }
        }
    }

    fun applyFilters(newFilters: FilterState) {
        filters = newFilters
        filteredPrograms = applyFiltersToList(programs, newFilters)
    }

    private fun applyFiltersToList(
        source: List<WashProgram>,
        filters: FilterState
    ): List<WashProgram> {
        return source.filter { program ->

            val matchesTemperature =
                filters.temperatures.isEmpty() || program.temperature in filters.temperatures

            val matchesSpeed =
                filters.speeds.isEmpty() || program.spinSpeed in filters.speeds

            val matchesDuration =
                program.durationMinutes in filters.minDuration..filters.maxDuration

            matchesTemperature && matchesSpeed && matchesDuration
        }
    }
}