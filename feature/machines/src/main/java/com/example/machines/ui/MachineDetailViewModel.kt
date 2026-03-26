package com.example.machines.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.TokenStorage
import com.example.domain.model.Machine
import com.example.domain.model.MachineSlot
import com.example.domain.model.WashProgram
import com.example.domain.repository.MachinesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.HttpException

@HiltViewModel
class MachineDetailViewModel @Inject constructor(
    private val repository: MachinesRepository,
    private val storage: TokenStorage
) : ViewModel() {

    var program by mutableStateOf<WashProgram?>(null)
        private set

    var selectedDate by mutableStateOf<String?>(null)
        private set

    var machines by mutableStateOf<List<Machine>>(emptyList())
        private set

    var selectedMachine by mutableStateOf<Machine?>(null)
        private set

    var slots by mutableStateOf<List<MachineSlot>>(emptyList())
        private set

    var selectedSlot by mutableStateOf<MachineSlot?>(null)
        private set

    var isLoadingMachines by mutableStateOf(false)
        private set

    var isLoadingSlots by mutableStateOf(false)
        private set

    var isBooking by mutableStateOf(false)
        private set

    var bookingConfirmation by mutableStateOf<BookingConfirmationUi?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadProgram(id: String) {
        viewModelScope.launch {
            try {
                program = repository.getProgramById(id)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Не удалось загрузить программу"
            }
        }
    }

    fun selectDate(date: String) {
        selectedDate = date
        selectedMachine = null
        selectedSlot = null
        slots = emptyList()
        bookingConfirmation = null
        loadMachines(date)
    }

    private fun loadMachines(date: String) {
        viewModelScope.launch {
            isLoadingMachines = true
            errorMessage = null
            try {
                machines = repository.getAvailableMachines(date)
                    .sortedBy { it.id }
            } catch (e: HttpException) {
                errorMessage = "Ошибка сервера: ${e.code()}"
            } catch (e: Exception) {
                errorMessage = e.message ?: "Не удалось загрузить машины"
            } finally {
                isLoadingMachines = false
            }
        }
    }

    fun selectMachine(machine: Machine) {
        selectedMachine = machine
        selectedSlot = null
        bookingConfirmation = null
        val date = selectedDate ?: return
        loadSlots(machine.id, date)
    }

    private fun loadSlots(machineId: Long, date: String) {
        val washTypeId = program?.id ?: return
        viewModelScope.launch {
            isLoadingSlots = true
            errorMessage = null
            try {
                slots = repository.getSlots(machineId, date,washTypeId)
            } catch (e: HttpException) {
                errorMessage = "Ошибка сервера: ${e.code()}"
            } catch (e: Exception) {
                errorMessage = e.message ?: "Не удалось загрузить слоты"
            } finally {
                isLoadingSlots = false
            }
        }
    }

    fun selectSlot(slot: MachineSlot) {
        selectedSlot = slot
        bookingConfirmation = null
    }

    fun bookSelectedSlot() {
        val currentProgram = program ?: return
        val currentSlot = selectedSlot ?: return
        val currentMachine = selectedMachine ?: return
        val currentDate = selectedDate ?: return

        viewModelScope.launch {
            isBooking = true
            try {
                val userId = storage.userIdFlow.firstOrNull() ?: return@launch

                repository.createBooking(
                    userId = userId,
                    slotId = currentSlot.id,
                    washTypeId = currentProgram.id
                )

                bookingConfirmation = BookingConfirmationUi(
                    date = currentDate,
                    time = formatTime(currentSlot.startTime),
                    machineName = currentMachine.name,
                    washTypeName = currentProgram.name,
                    price = currentProgram.price
                )

                loadSlots(currentMachine.id, currentDate)
                selectedSlot = null

            } catch (_: Exception) {

            } finally {
                isBooking = false
            }
        }
    }
    fun dismissBookingConfirmation() {
        bookingConfirmation = null
    }

    private fun formatTime(dateTime: String): String {
        return try {
            dateTime.substring(11, 16)
        } catch (e: Exception) {
            dateTime
        }
    }
}