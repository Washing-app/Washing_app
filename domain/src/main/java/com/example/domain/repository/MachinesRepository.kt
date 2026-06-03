package com.example.domain.repository

import com.example.domain.model.Machine
import com.example.domain.model.MachineSlot
import com.example.domain.model.PrepareBookingResult
import com.example.domain.model.WashProgram


interface MachinesRepository {
    suspend fun getPrograms(): List<WashProgram>
    suspend fun getProgramById(id: String): WashProgram?
    suspend fun getAvailableMachines(date: String): List<Machine>
    suspend fun getSlots(
        machineId: Long,
        date: String,
        washTypeId: Long
    ): List<MachineSlot>
    suspend fun prepareBooking(
        slotId: Long,
        washTypeId: Long
    ): PrepareBookingResult

    suspend fun createBooking(
        slotId: Long,
        washTypeId: Long
    ): String
    suspend fun holdSlot(slotId: Long)

    suspend fun releaseMyHold()
}