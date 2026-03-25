package com.example.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("auth")

class TokenStorage(
    private val context: Context
) {

    private val TOKEN_KEY = stringPreferencesKey("token")
    private val USER_ID_KEY = stringPreferencesKey("user_id")

    suspend fun saveAuthData(token: String, userId: String) {
        context.dataStore.edit {
            it[TOKEN_KEY] = token
            it[USER_ID_KEY] = userId
        }
    }

    val tokenFlow = context.dataStore.data.map {
        it[TOKEN_KEY]
    }

    val userIdFlow = context.dataStore.data.map {
        it[USER_ID_KEY]
    }

    suspend fun clearToken() {
        context.dataStore.edit {
            it.remove(TOKEN_KEY)
            it.remove(USER_ID_KEY)
        }
    }
}