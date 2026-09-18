package com.example.myapplication.data.repository

import com.example.myapplication.data.preferences.SettingsDataStore
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val settingsDataStore: SettingsDataStore) {
    val languageFlow: Flow<String> = settingsDataStore.languageFlow
    val roleFlow: Flow<String> = settingsDataStore.roleFlow

    suspend fun setLanguage(language: String) {
        settingsDataStore.setLanguage(language)
    }

    suspend fun setRole(role: String) {
        settingsDataStore.setRole(role)
    }
}
