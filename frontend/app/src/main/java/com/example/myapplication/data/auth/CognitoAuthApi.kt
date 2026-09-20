package com.example.myapplication.data.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/**
 * Tokens returned by Cognito after successful authentication.
 */
data class CognitoTokens(
    val idToken: String,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int
)

/** Thrown when the username or password is incorrect. */
class InvalidCredentialsException : Exception("Invalid username or password")

/** Thrown on network-level failures (no connectivity, timeout). */
class CognitoNetworkException(message: String) : Exception(message)

/**
 * Calls the Amazon Cognito User Pools HTTP API directly via OkHttp.
 *
 * Uses the `InitiateAuth` action with `USER_PASSWORD_AUTH` flow —
 * no AWS SDK needed. Compatible with any compileSdk / AGP version.
 */
class CognitoAuthApi {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/x-amz-json-1.1".toMediaType()

    // ---- Public API ---------------------------------------------------

    /**
     * Authenticate a user with username + password.
     *
     * @throws InvalidCredentialsException on wrong credentials
     * @throws CognitoNetworkException     on connectivity issues
     * @throws Exception                   on other Cognito errors
     */
    suspend fun signIn(username: String, password: String): CognitoTokens {
        return withContext(Dispatchers.IO) {
            val body = JSONObject().apply {
                put("AuthFlow", "USER_PASSWORD_AUTH")
                put("ClientId", CognitoConfig.clientId)
                put("AuthParameters", JSONObject().apply {
                    put("USERNAME", username)
                    put("PASSWORD", password)
                })
            }

            executeAuthRequest(body, "AWSCognitoIdentityProviderService.InitiateAuth")
        }
    }

    /**
     * Register a new user with Cognito via self-service sign up.
     * Passes the custom:role attribute.
     */
    suspend fun signUp(email: String, password: String, role: String) {
        return withContext(Dispatchers.IO) {
            val body = JSONObject().apply {
                put("ClientId", CognitoConfig.clientId)
                put("Username", email)
                put("Password", password)
            }

            val request = Request.Builder()
                .url(CognitoConfig.cognitoEndpoint)
                .post(body.toString().toRequestBody(jsonMediaType))
                .addHeader("X-Amz-Target", "AWSCognitoIdentityProviderService.SignUp")
                .addHeader("Content-Type", "application/x-amz-json-1.1")
                .build()

            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    val responseBody = response.body?.string() ?: ""
                    val errorJson = try { JSONObject(responseBody) } catch (_: Exception) { null }
                    val errorMsg = errorJson?.optString("message", "Registration failed") ?: "Registration failed"
                    throw Exception(errorMsg)
                }
            } catch (e: UnknownHostException) {
                throw CognitoNetworkException("No internet connection.")
            } catch (e: SocketTimeoutException) {
                throw CognitoNetworkException("The connection timed out.")
            } catch (e: IOException) {
                throw CognitoNetworkException("A network error occurred.")
            }
        }
    }

    /**
     * Confirm a new user registration using a verification code.
     */
    suspend fun confirmSignUp(email: String, code: String) {
        return withContext(Dispatchers.IO) {
            val body = JSONObject().apply {
                put("ClientId", CognitoConfig.clientId)
                put("Username", email)
                put("ConfirmationCode", code)
            }

            val request = Request.Builder()
                .url(CognitoConfig.cognitoEndpoint)
                .post(body.toString().toRequestBody(jsonMediaType))
                .addHeader("X-Amz-Target", "AWSCognitoIdentityProviderService.ConfirmSignUp")
                .addHeader("Content-Type", "application/x-amz-json-1.1")
                .build()

            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    val responseBody = response.body?.string() ?: ""
                    val errorJson = try { JSONObject(responseBody) } catch (_: Exception) { null }
                    val errorMsg = errorJson?.optString("message", "Verification failed") ?: "Verification failed"
                    throw Exception(errorMsg)
                }
            } catch (e: UnknownHostException) {
                throw CognitoNetworkException("No internet connection.")
            } catch (e: SocketTimeoutException) {
                throw CognitoNetworkException("The connection timed out.")
            } catch (e: IOException) {
                throw CognitoNetworkException("A network error occurred.")
            }
        }
    }

    /**
     * Refresh an authenticated session using the refresh token.
     * The returned [CognitoTokens.refreshToken] is the same as the input
     * because Cognito does not rotate refresh tokens.
     */
    suspend fun refreshSession(refreshToken: String): CognitoTokens {
        return withContext(Dispatchers.IO) {
            val body = JSONObject().apply {
                put("AuthFlow", "REFRESH_TOKEN_AUTH")
                put("ClientId", CognitoConfig.clientId)
                put("AuthParameters", JSONObject().apply {
                    put("REFRESH_TOKEN", refreshToken)
                })
            }

            val tokens = executeAuthRequest(
                body,
                "AWSCognitoIdentityProviderService.InitiateAuth"
            )
            // Cognito does not return a new refresh token on refresh
            tokens.copy(refreshToken = refreshToken)
        }
    }

    // ---- Internal -----------------------------------------------------

    private fun executeAuthRequest(
        requestBody: JSONObject,
        amzTarget: String
    ): CognitoTokens {
        val request = Request.Builder()
            .url(CognitoConfig.cognitoEndpoint)
            .post(requestBody.toString().toRequestBody(jsonMediaType))
            .addHeader("X-Amz-Target", amzTarget)
            .addHeader("Content-Type", "application/x-amz-json-1.1")
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
                ?: throw Exception("Empty response from Cognito")

            if (response.isSuccessful) {
                val json = JSONObject(responseBody)
                val result = json.getJSONObject("AuthenticationResult")
                return CognitoTokens(
                    idToken = result.getString("IdToken"),
                    accessToken = result.getString("AccessToken"),
                    refreshToken = result.optString("RefreshToken", ""),
                    expiresIn = result.getInt("ExpiresIn")
                )
            }

            // ---- Error handling ----
            val errorJson = try {
                JSONObject(responseBody)
            } catch (_: Exception) {
                null
            }
            val errorType = errorJson?.optString("__type", "") ?: ""
            val errorMsg = errorJson?.optString("message", "Authentication failed")
                ?: "Authentication failed"

            when {
                "NotAuthorizedException" in errorType ->
                    throw InvalidCredentialsException()

                "UserNotFoundException" in errorType ->
                    throw InvalidCredentialsException()

                "UserNotConfirmedException" in errorType ->
                    throw Exception(
                        "Your account has not been confirmed yet. " +
                                "Please check your email for a verification link."
                    )

                "PasswordResetRequiredException" in errorType ->
                    throw Exception(
                        "A password reset is required. " +
                                "Please reset your password before signing in."
                    )

                else -> throw Exception(errorMsg)
            }
        } catch (e: InvalidCredentialsException) {
            throw e
        } catch (e: UnknownHostException) {
            throw CognitoNetworkException(
                "No internet connection. Please check your network and try again."
            )
        } catch (e: SocketTimeoutException) {
            throw CognitoNetworkException(
                "The connection timed out. Please try again."
            )
        } catch (e: IOException) {
            throw CognitoNetworkException(
                "A network error occurred. Please check your connection and try again."
            )
        }
        // Other exceptions (e.g. JSONException) propagate naturally.
    }
}
