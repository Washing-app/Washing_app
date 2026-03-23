package com.example.machines.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.WashProgram
import com.example.domain.repository.MachinesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineDetailViewModel @Inject constructor(
    private val repository: MachinesRepository
) : ViewModel() {

    var program by mutableStateOf<WashProgram?>(null)
        private set

    var selectedDate by mutableStateOf<String?>(null)
        private set

    var selectedMachine by mutableStateOf<Int?>(null)
        private set

    init {
        // позже загрузим по id
    }

    fun loadProgram(id: String) {
        viewModelScope.launch {
            program = repository.getPrograms().find { it.id == id }
        }
    }

    fun selectDate(date: String) {
        selectedDate = date
    }

    fun selectMachine(machine: Int) {
        selectedMachine = machine
    }
}