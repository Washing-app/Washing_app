package com.example.domain.repository

import com.example.domain.model.WashProgram


interface MachinesRepository {
    suspend fun getPrograms(): List<WashProgram>
}