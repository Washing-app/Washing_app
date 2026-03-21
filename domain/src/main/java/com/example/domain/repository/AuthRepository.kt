package com.example.domain.repository

interface AuthRepository {
    suspend fun login(phone: String, password: String): Boolean
}