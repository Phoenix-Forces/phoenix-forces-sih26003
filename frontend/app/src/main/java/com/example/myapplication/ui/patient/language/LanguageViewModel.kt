package com.example.myapplication.ui.patient.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.PatientRepository
import com.example.myapplication.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LanguageUiState(
    val selectedLanguage: String = "en"
)

class LanguageViewModel(
    private val settingsRepository: SettingsRepository,
    private val patientRepository: PatientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LanguageUiState())
    val uiState: StateFlow<LanguageUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.languageFlow.collect { lang ->
                _uiState.value = LanguageUiState(selectedLanguage = lang)
            }
        }
    }

    fun selectLanguage(languageCode: String) {
        viewModelScope.launch {
            settingsRepository.setLanguage(languageCode)
            patientRepository.updateLanguage(languageCode)
            _uiState.value = LanguageUiState(selectedLanguage = languageCode)
        }
    }
}
