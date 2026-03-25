package com.example.data.repository

import com.example.data.local.TokenStorage
import com.example.data.remote.api.AuthApi
import com.example.data.remote.dto.LoginRequest
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import okhttp3.internal.userAgent
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val storage: TokenStorage
) : AuthRepository {

    override suspend fun login(phone: String, password: String): Boolean {
        return try {
            val response = api.login(
                LoginRequest(phone, password)
            )

            storage.saveAuthData(
                token = response.token,
                userId = response.userId
            )

            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return storage.tokenFlow.first() != null
    }

    override suspend fun logout() {
        storage.clearToken()
    }
}