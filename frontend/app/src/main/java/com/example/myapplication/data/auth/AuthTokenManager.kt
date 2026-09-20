package com.example.myapplication.data.auth

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import org.json.JSONObject
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Manages authentication tokens with AES-256-GCM encryption backed by
 * the Android Keystore.
 *
 * Uses zero external dependencies — only standard Android APIs available
 * since API 23 (project minSdk is 24). This avoids pulling in
 * `security-crypto` alpha artifacts or heavyweight Tink bundles.
 *
 * Tokens are stored in a private [SharedPreferences] file; the actual
 * values are encrypted with a hardware-backed (where available) AES key
 * that never leaves the Keystore.
 */
class AuthTokenManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )

    private val keyStore: KeyStore =
        KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

    // ---- Keystore key management --------------------------------------

    private fun getOrCreateSecretKey(): SecretKey {
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val spec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()

            KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
                .apply { init(spec); generateKey() }
        }
        return (keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
    }

    // ---- AES-256-GCM encrypt / decrypt --------------------------------

    private fun encrypt(plaintext: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey())
        val iv = cipher.iv                                          // 12 bytes
        val ciphertext = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))
        val combined = ByteArray(iv.size + ciphertext.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(ciphertext, 0, combined, iv.size, ciphertext.size)
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    private fun decrypt(encoded: String): String? = try {
        val combined = Base64.decode(encoded, Base64.NO_WRAP)
        if (combined.size <= GCM_IV_LENGTH) null
        else {
            val iv = combined.copyOfRange(0, GCM_IV_LENGTH)
            val ciphertext = combined.copyOfRange(GCM_IV_LENGTH, combined.size)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(
                Cipher.DECRYPT_MODE,
                getOrCreateSecretKey(),
                GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
            )
            String(cipher.doFinal(ciphertext), Charsets.UTF_8)
        }
    } catch (_: Exception) {
        // Key was rotated, data corrupted, or device restored from backup.
        // Returning null forces a fresh sign-in.
        null
    }

    // ---- Token storage ------------------------------------------------

    fun saveTokens(
        idToken: String,
        accessToken: String,
        refreshToken: String,
        expiresIn: Int
    ) {
        val expiresAt = System.currentTimeMillis() + (expiresIn * 1000L)
        prefs.edit()
            .putString(KEY_ID_TOKEN, encrypt(idToken))
            .putString(KEY_ACCESS_TOKEN, encrypt(accessToken))
            .putString(KEY_REFRESH_TOKEN, encrypt(refreshToken))
            .putLong(KEY_EXPIRES_AT, expiresAt)
            .apply()
    }

    fun getAccessToken(): String? {
        val enc = prefs.getString(KEY_ACCESS_TOKEN, null) ?: return null
        return decrypt(enc)
    }

    fun getIdToken(): String? {
        val enc = prefs.getString(KEY_ID_TOKEN, null) ?: return null
        return decrypt(enc)
    }

    fun getRefreshToken(): String? {
        val enc = prefs.getString(KEY_REFRESH_TOKEN, null) ?: return null
        return decrypt(enc)
    }

    /**
     * Returns `true` when the user has a persisted session
     * (tokens exist and are either still valid or can be refreshed).
     */
    fun isLoggedIn(): Boolean {
        val hasAccessToken = prefs.getString(KEY_ACCESS_TOKEN, null) != null
        if (!hasAccessToken) return false
        val hasRefreshToken = prefs.getString(KEY_REFRESH_TOKEN, null) != null
        val expiresAt = prefs.getLong(KEY_EXPIRES_AT, 0L)
        return System.currentTimeMillis() < expiresAt || hasRefreshToken
    }

    fun isAccessTokenExpired(): Boolean {
        val expiresAt = prefs.getLong(KEY_EXPIRES_AT, 0L)
        return System.currentTimeMillis() >= expiresAt
    }

    fun clearTokens() {
        prefs.edit().clear().apply()
    }

    // ---- Demo mode flag -----------------------------------------------

    fun setDemoMode(enabled: Boolean, role: String = "patient") {
        prefs.edit()
            .putBoolean(KEY_DEMO_MODE, enabled)
            .putString(KEY_DEMO_ROLE, role)
            .apply()
    }

    fun isDemoMode(): Boolean = prefs.getBoolean(KEY_DEMO_MODE, false)

    // ---- JWT claim helpers --------------------------------------------

    /**
     * Extracts the user's role from the `cognito:groups` claim
     * in the stored ID token. Defaults to `"patient"`.
     */
    fun getUserRole(): String {
        if (isDemoMode()) {
            return prefs.getString(KEY_DEMO_ROLE, "patient") ?: "patient"
        }
        val idToken = getIdToken() ?: return "patient"
        return try {
            val payload = decodeJwtPayload(idToken)
            val groups = payload.optJSONArray("cognito:groups")
            if (groups != null) {
                for (i in 0 until groups.length()) {
                    if ("caregiver" in groups.getString(i).lowercase()) {
                        return "caregiver"
                    }
                }
            }
            // Also check custom:role attribute as a fallback
            val customRole = payload.optString("custom:role", "")
            if ("caregiver" in customRole.lowercase()) "caregiver" else "patient"
        } catch (_: Exception) {
            "patient"
        }
    }

    /** Extracts the `email` claim from the ID token. */
    fun getUserEmail(): String {
        val idToken = getIdToken() ?: return ""
        return try {
            decodeJwtPayload(idToken).optString("email", "")
        } catch (_: Exception) {
            ""
        }
    }

    /**
     * Decodes the payload (middle part) of a JWT.
     * Does NOT verify the signature — that is the backend's responsibility.
     */
    private fun decodeJwtPayload(token: String): JSONObject {
        val parts = token.split(".")
        require(parts.size >= 2) { "Invalid JWT format" }
        val bytes = Base64.decode(
            parts[1],
            Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        )
        return JSONObject(String(bytes, Charsets.UTF_8))
    }

    companion object {
        private const val PREFS_NAME = "arogya_auth_secure"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "arogya_cognito_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH_BITS = 128

        private const val KEY_ID_TOKEN = "enc_id_token"
        private const val KEY_ACCESS_TOKEN = "enc_access_token"
        private const val KEY_REFRESH_TOKEN = "enc_refresh_token"
        private const val KEY_EXPIRES_AT = "token_expires_at"
        private const val KEY_DEMO_MODE = "is_demo_mode"
        private const val KEY_DEMO_ROLE = "demo_role"
    }
}
