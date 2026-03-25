package com.example.domain.model

data class WashProgram(
    val id: Long,
    val name: String,
    val durationMinutes: Int,
    val price: Int,
    val temperature: Int,
    val spinSpeed: Int,
    val description: String,
    val imageLink: String?
)
