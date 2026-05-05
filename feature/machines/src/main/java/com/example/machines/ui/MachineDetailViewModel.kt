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
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import android.content.Context
import com.example.common.util.notification.WashingReminderScheduler

import dagger.hilt.android.qualifiers.ApplicationContext

@HiltViewModel
class MachineDetailViewModel @Inject constructor(
    private val repository: MachinesRepository,
    private val storage: TokenStorage,
    @ApplicationContext private val context: Context
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

    var bookingFinishUi by mutableStateOf<BookingFinishUi?>(null)
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
                val prepare = repository.prepareBooking(
                    slotId = currentSlot.id,
                    washTypeId = currentProgram.id
                )

                if (prepare.blockedByUnpaidBooking) {
                    bookingFinishUi = BookingFinishUi(
                        mode = BookingFinishMode.BLOCKED_BY_UNPAID,
                        bookingIdForPayment = prepare.existingUnpaidBookingId,
                        date = currentDate,
                        time = formatTime(currentSlot.startTime),
                        machineName = currentMachine.name,
                        washTypeName = currentProgram.name,
                        price = currentProgram.price
                    )
                    return@launch
                }

                if (!prepare.paymentRequired) {
                    val bookingId = repository.createBooking(
                        slotId = currentSlot.id,
                        washTypeId = currentProgram.id
                    )

                    WashingReminderScheduler(context).schedule(
                        bookingId = bookingId,
                        startTime = currentSlot.startTime,
                        endTime = currentSlot.endTime
                    )

                    bookingFinishUi = BookingFinishUi(
                        mode = BookingFinishMode.FIRST_BOOKING,
                        bookingIdForPayment = null,
                        date = currentDate,
                        time = formatTime(currentSlot.startTime),
                        machineName = currentMachine.name,
                        washTypeName = currentProgram.name,
                        price = currentProgram.price
                    )

                    loadSlots(currentMachine.id, currentDate)
                    selectedSlot = null
                    return@launch
                }

                bookingFinishUi = BookingFinishUi(
                    mode = BookingFinishMode.REQUIRE_PAYMENT,
                    bookingIdForPayment = null,
                    date = currentDate,
                    time = formatTime(currentSlot.startTime),
                    machineName = currentMachine.name,
                    washTypeName = currentProgram.name,
                    price = currentProgram.price
                )

            } catch (_: Exception) {
            } finally {
                isBooking = false
            }
        }
    }

    fun finishFirstBookingAndGoHome(onDone: () -> Unit) {
        bookingFinishUi = null
        selectedSlot = null
        onDone()
    }

    fun createPaidBookingAndOpenPayment(onDone: (String) -> Unit) {
        val currentProgram = program ?: return
        val currentSlot = selectedSlot ?: return
        val currentMachine = selectedMachine ?: return
        val currentDate = selectedDate ?: return

        viewModelScope.launch {
            try {
                val bookingId = repository.createBooking(
                    slotId = currentSlot.id,
                    washTypeId = currentProgram.id
                )
                WashingReminderScheduler(context).schedule(
                    bookingId = bookingId,
                    startTime = currentSlot.startTime,
                    endTime = currentSlot.endTime
                )
                loadSlots(currentMachine.id, currentDate)
                selectedSlot = null
                bookingFinishUi = null
                onDone(bookingId)
            } catch (_: Exception) {
            }
        }
    }

    fun dismissBookingFinishUi() {
        bookingFinishUi = null
    }

    private fun formatTime(dateTime: String): String {
        return try {
            dateTime.substring(11, 16)
        } catch (_: Exception) {
            dateTime
        }
    }
}