package com.example.data.repository

import com.example.domain.repository.AuthRepository

import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override suspend fun login(phone: String, password: String): Boolean {
        return phone == "123" && password == "123"
    }
}