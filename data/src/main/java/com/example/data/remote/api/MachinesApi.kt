package com.example.data.remote.api

import com.example.data.remote.dto.MachineDto
import com.example.data.remote.dto.MachineSlotDto
import com.example.data.remote.dto.WashProgramDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MachinesApi {

    @GET("wash-types")
    suspend fun getPrograms(): List<WashProgramDto>

    @GET("machines/available")
    suspend fun getAvailableMachines(
        @Query("date") date: String
    ): List<MachineDto>

    @GET("machines/{machineId}/slots")
    suspend fun getSlots(
        @Path("machineId") machineId: Long,
        @Query("date") date: String
    ): List<MachineSlotDto>
}