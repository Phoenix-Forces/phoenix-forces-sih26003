package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.auth.AuthRepository
import com.example.myapplication.data.auth.AuthTokenManager
import com.example.myapplication.data.auth.CognitoAuthApi
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.preferences.SettingsDataStore
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.data.repository.AlertRepository
import com.example.myapplication.data.repository.GameRepository
import com.example.myapplication.data.repository.PatientRepository
import com.example.myapplication.data.repository.ReminderRepository
import com.example.myapplication.data.repository.SettingsRepository
import com.example.myapplication.utils.VoiceAssistantManager

class ArogyaApplication : Application() {

    val database by lazy { AppDatabase.getInstance(this) }
    val settingsDataStore by lazy { SettingsDataStore(this) }

    val patientRepository by lazy { PatientRepository(database.patientDao()) }
    val reminderRepository by lazy { ReminderRepository(database.reminderDao()) }
    val gameRepository by lazy { GameRepository(database.gameResultDao()) }
    val alertRepository by lazy { AlertRepository(database.alertDao()) }
    val settingsRepository by lazy { SettingsRepository(settingsDataStore) }

    val voiceAssistantManager by lazy { VoiceAssistantManager(this) }

    // ---- Authentication ----

    val cognitoAuthApi by lazy { CognitoAuthApi() }

    val authTokenManager by lazy {
        AuthTokenManager(this).also { manager ->
            // Wire the token provider into Retrofit so every API call
            // automatically carries the Bearer header when authenticated.
            RetrofitClient.setTokenProvider { manager.getAccessToken() }
        }
    }

    val authRepository by lazy {
        AuthRepository(cognitoAuthApi, authTokenManager)
    }
}
