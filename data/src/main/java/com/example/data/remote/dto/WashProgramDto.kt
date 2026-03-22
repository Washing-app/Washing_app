package com.example.data.remote.dto

import com.example.domain.model.WashProgram

data class WashProgramDto(
    val name: String,
    val durationMinutes: Int,
    val price: Int,
    val temperature: Int,
    val spinSpeed: Int,
    val description: String
)

fun WashProgramDto.toDomain() = WashProgram(
    name = name,
    durationMinutes = durationMinutes,
    price = price,
    temperature = temperature,
    spinSpeed = spinSpeed,
    description = description
)
