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

    var searchQuery by mutableStateOf("")
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
                updateFilteredPrograms()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Не удалось загрузить программы"
            } finally {
                isLoading = false
            }
        }
    }

    fun applyFilters(newFilters: FilterState) {
        filters = newFilters
        updateFilteredPrograms()
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
        updateFilteredPrograms()
    }

    private fun updateFilteredPrograms() {
        filteredPrograms = applyFiltersToList(
            source = programs,
            filters = filters,
            query = searchQuery
        )
    }

    private fun applyFiltersToList(
        source: List<WashProgram>,
        filters: FilterState,
        query: String
    ): List<WashProgram> {
        val normalizedQuery = query.trim().lowercase()

        return source.filter { program ->

            val matchesTemperature =
                filters.temperatures.isEmpty() || program.temperature in filters.temperatures

            val matchesSpeed =
                filters.speeds.isEmpty() || program.spinSpeed in filters.speeds

            val matchesDuration =
                program.durationMinutes in filters.minDuration..filters.maxDuration

            val matchesQuery =
                normalizedQuery.isBlank() ||
                        program.name.lowercase().contains(normalizedQuery)
                        || program.description.lowercase().contains(normalizedQuery)

            matchesTemperature && matchesSpeed && matchesDuration && matchesQuery
        }
    }
}