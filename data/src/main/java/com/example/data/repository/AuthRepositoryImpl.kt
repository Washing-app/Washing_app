package com.example.data.repository

import com.example.data.remote.api.AuthApi
import com.example.data.remote.dto.LoginRequest
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun login(phone: String, password: String): Boolean {
        return try {
            val response = api.login(
                LoginRequest(phone, password)
            )
            response.token.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }
}