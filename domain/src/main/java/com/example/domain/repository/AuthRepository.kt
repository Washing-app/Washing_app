package com.example.domain.repository

interface AuthRepository {
    suspend fun login(phone: String, password: String): Boolean
    suspend fun isLoggedIn(): Boolean
    suspend fun logout()
}