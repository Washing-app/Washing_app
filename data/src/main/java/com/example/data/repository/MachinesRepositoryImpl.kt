package com.example.data.repository

import com.example.data.remote.api.MachinesApi
import com.example.data.remote.dto.CreateBookingRequest
import com.example.data.remote.dto.toDomain
import com.example.domain.model.Machine
import com.example.domain.model.MachineSlot
import com.example.domain.model.WashProgram
import com.example.domain.repository.MachinesRepository
import javax.inject.Inject

class MachinesRepositoryImpl @Inject constructor(
    private val api: MachinesApi
) : MachinesRepository {

    override suspend fun getPrograms(): List<WashProgram> {
        return api.getPrograms().map { it.toDomain() }
    }

    override suspend fun getProgramById(id: String): WashProgram? {
        return getPrograms().find { it.id == id.toLong() }
    }

    override suspend fun getAvailableMachines(date: String): List<Machine> {
        return api.getAvailableMachines(date).map { it.toDomain() }
    }

    override suspend fun getSlots(
        machineId: Long,
        date: String,
        washTypeId: Long
    ): List<MachineSlot> {
        return api.getSlots(machineId, date, washTypeId).map { it.toDomain() }
    }

    override suspend fun createBooking(
        userId: String,
        slotId: Long,
        washTypeId: Long
    ) {
        api.createBooking(
            CreateBookingRequest(
                userId = userId,
                slotId = slotId,
                washTypeId = washTypeId
            )
        )
    }
}