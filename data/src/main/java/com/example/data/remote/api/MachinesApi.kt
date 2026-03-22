package com.example.data.remote.api

import com.example.data.remote.dto.WashProgramDto
import retrofit2.http.GET

interface MachinesApi {

    @GET("wash-types")
    suspend fun getPrograms(): List<WashProgramDto>
}