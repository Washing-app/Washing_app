package com.example.data.repository

import com.example.data.remote.api.MachinesApi
import com.example.data.remote.dto.toDomain
import com.example.domain.model.WashProgram
import com.example.domain.repository.MachinesRepository
import javax.inject.Inject

class MachinesRepositoryImpl @Inject constructor(
    private val api: MachinesApi
) : MachinesRepository {

    override suspend fun getPrograms(): List<WashProgram> {
        return api.getPrograms().map { it.toDomain() }
    }
}