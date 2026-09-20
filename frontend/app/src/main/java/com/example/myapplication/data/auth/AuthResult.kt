package com.example.myapplication.data.auth

/**
 * Represents the outcome of an authentication attempt.
 */
sealed class AuthResult {

    /** Authentication succeeded. [role] is "patient" or "caregiver". */
    data class Success(val role: String) : AuthResult()

    /** Username or password was incorrect. */
    data object InvalidCredentials : AuthResult()

    /** A network-level error occurred (no connectivity, timeout, etc.). */
    data class NetworkError(val message: String) : AuthResult()

    /** Any other error (account not confirmed, pool misconfigured, etc.). */
    data class Error(val message: String) : AuthResult()
}

/**
 * Represents the outcome of a registration attempt.
 */
sealed class RegisterResult {
    data object Success : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}
