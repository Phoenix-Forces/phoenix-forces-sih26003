package com.example.myapplication.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.auth.AuthRepository
import com.example.myapplication.data.auth.AuthResult
import com.example.myapplication.data.auth.CognitoConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false,
    val role: String = ""
)

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun signIn(username: String, password: String) {
        if (_uiState.value.isLoading) return

        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState(
                error = "Please enter both email and password."
            )
            return
        }

        if (!CognitoConfig.isConfigured) {
            _uiState.value = LoginUiState(
                error = "Authentication is not configured. " +
                        "Add Cognito credentials to local.properties, " +
                        "or use Demo Mode."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)

            _uiState.value = when (val result = authRepository.signIn(username, password)) {
                is AuthResult.Success -> LoginUiState(
                    loginSuccess = true,
                    role = result.role
                )

                is AuthResult.InvalidCredentials -> LoginUiState(
                    error = "Invalid email or password. " +
                            "Please check your credentials and try again."
                )

                is AuthResult.NetworkError -> LoginUiState(error = result.message)

                is AuthResult.Error -> LoginUiState(error = result.message)
            }
        }
    }

    fun enterDemoMode(role: String = "patient") {
        authRepository.enterDemoMode(role)
        _uiState.value = LoginUiState(loginSuccess = true, role = role)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun reset() {
        _uiState.value = LoginUiState()
    }
}
