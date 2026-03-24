package com.example.domain.repository

import com.example.domain.model.Machine
import com.example.domain.model.MachineSlot
import com.example.domain.model.WashProgram


interface MachinesRepository {
    suspend fun getPrograms(): List<WashProgram>
    suspend fun getProgramById(id: String): WashProgram?
    suspend fun getAvailableMachines(date: String): List<Machine>
    suspend fun getSlots(machineId: Long, date: String): List<MachineSlot>
}