package com.example.washing_app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.*

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    var startDestination by mutableStateOf<String?>(null)
        private set

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            val isLoggedIn = repository.isLoggedIn()

            startDestination = if (isLoggedIn) {
                "machines"
            } else {
                "auth"
            }
        }
    }
}