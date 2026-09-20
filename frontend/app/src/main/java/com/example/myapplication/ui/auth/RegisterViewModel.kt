package com.example.myapplication.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.auth.AuthRepository
import com.example.myapplication.data.auth.CognitoConfig
import com.example.myapplication.data.auth.RegisterResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val registerSuccess: Boolean = false,
    val verificationSuccess: Boolean = false,
    val email: String = ""
)

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun signUp(email: String, password: String, confirm: String, role: String) {
        if (_uiState.value.isLoading) return

        if (email.isBlank() || password.isBlank() || confirm.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please fill out all fields.")
            return
        }

        if (password != confirm) {
            _uiState.value = _uiState.value.copy(error = "Passwords do not match.")
            return
        }

        if (password.length < 8) {
            _uiState.value = _uiState.value.copy(error = "Password must be at least 8 characters.")
            return
        }

        if (!CognitoConfig.isConfigured) {
            _uiState.value = _uiState.value.copy(
                error = "Authentication is not configured. Add Cognito credentials to local.properties."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, email = email)

            val result = authRepository.signUp(email, password, role)
            _uiState.value = when (result) {
                is RegisterResult.Success -> _uiState.value.copy(
                    isLoading = false,
                    registerSuccess = true
                )
                is RegisterResult.Error -> _uiState.value.copy(
                    isLoading = false,
                    error = result.message
                )
            }
        }
    }

    fun confirmSignUp(code: String) {
        if (_uiState.value.isLoading) return

        if (code.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please enter the verification code.")
            return
        }

        val email = _uiState.value.email
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Email is missing. Please restart registration.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = authRepository.confirmSignUp(email, code)
            _uiState.value = when (result) {
                is RegisterResult.Success -> _uiState.value.copy(
                    isLoading = false,
                    verificationSuccess = true
                )
                is RegisterResult.Error -> _uiState.value.copy(
                    isLoading = false,
                    error = result.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun reset() {
        _uiState.value = RegisterUiState()
    }
}
