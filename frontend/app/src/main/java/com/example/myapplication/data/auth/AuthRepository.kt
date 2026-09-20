package com.example.myapplication.data.auth

/**
 * Orchestrates authentication operations:
 *  • Sign in via Cognito
 *  • Token persistence (delegated to [AuthTokenManager])
 *  • Sign out / demo-mode toggling
 */
class AuthRepository(
    private val cognitoAuthApi: CognitoAuthApi,
    private val authTokenManager: AuthTokenManager
) {

    /**
     * Authenticates the user against Cognito and stores the resulting
     * tokens. Returns an [AuthResult] with the detected role.
     */
    suspend fun signIn(username: String, password: String): AuthResult {
        return try {
            val tokens = cognitoAuthApi.signIn(username, password)
            authTokenManager.saveTokens(
                idToken = tokens.idToken,
                accessToken = tokens.accessToken,
                refreshToken = tokens.refreshToken,
                expiresIn = tokens.expiresIn
            )
            authTokenManager.setDemoMode(false)

            val role = authTokenManager.getUserRole()
            AuthResult.Success(role = role)
        } catch (e: InvalidCredentialsException) {
            AuthResult.InvalidCredentials
        } catch (e: CognitoNetworkException) {
            AuthResult.NetworkError(e.message ?: "Network error occurred")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    /**
     * Registers a new user.
     */
    suspend fun signUp(email: String, password: String, role: String): RegisterResult {
        return try {
            cognitoAuthApi.signUp(email, password, role)
            RegisterResult.Success
        } catch (e: CognitoNetworkException) {
            RegisterResult.Error(e.message ?: "Network error occurred")
        } catch (e: Exception) {
            RegisterResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    /**
     * Confirms a newly registered user using a verification code.
     */
    suspend fun confirmSignUp(email: String, code: String): RegisterResult {
        return try {
            cognitoAuthApi.confirmSignUp(email, code)
            RegisterResult.Success
        } catch (e: CognitoNetworkException) {
            RegisterResult.Error(e.message ?: "Network error occurred")
        } catch (e: Exception) {
            RegisterResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    /** Clears all stored tokens and the demo-mode flag. */
    fun signOut() {
        authTokenManager.clearTokens()
    }

    /** Enters demo mode — clears any real tokens first. */
    fun enterDemoMode(role: String = "patient") {
        authTokenManager.clearTokens()
        authTokenManager.setDemoMode(true, role)
    }

    /** `true` when valid (or refreshable) tokens are stored. */
    fun isAuthenticated(): Boolean = authTokenManager.isLoggedIn()

    /** `true` when the user selected "Continue as Demo". */
    fun isDemoMode(): Boolean = authTokenManager.isDemoMode()

    /** Returns `"patient"` or `"caregiver"` based on JWT claims. */
    fun getUserRole(): String = authTokenManager.getUserRole()
}
