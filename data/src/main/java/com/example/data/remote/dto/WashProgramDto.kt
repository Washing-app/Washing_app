package com.example.data.remote.dto

import com.example.domain.model.WashProgram

data class WashProgramDto(
    val id: String,
    val name: String,
    val durationMinutes: Int,
    val price: Int,
    val temperature: Int,
    val spinSpeed: Int,
    val description: String,
    val imageLink: String?
)

fun WashProgramDto.toDomain() = WashProgram(
    id = id,
    name = name,
    durationMinutes = durationMinutes,
    price = price,
    temperature = temperature,
    spinSpeed = spinSpeed,
    description = description,
    imageLink = imageLink,

)
