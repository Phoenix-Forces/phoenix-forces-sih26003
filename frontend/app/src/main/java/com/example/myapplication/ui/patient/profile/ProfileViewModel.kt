package com.example.myapplication.ui.patient.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.PatientEntity
import com.example.myapplication.data.repository.PatientRepository
import com.example.myapplication.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val name: String = "Ramesh Kumar",
    val age: Int = 72,
    val caregiverName: String = "Anita Kumar",
    val emergencyContact: String = "+91 98765 43210",
    val primaryLanguage: String = "en",
    val streakDays: Int = 5,
    val isEditing: Boolean = false
)

class ProfileViewModel(
    private val patientRepository: PatientRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            patientRepository.patientFlow.collect { patient ->
                if (patient != null) {
                    _uiState.value = _uiState.value.copy(
                        name = patient.name,
                        age = patient.age,
                        caregiverName = patient.caregiverName,
                        primaryLanguage = patient.primaryLanguage,
                        streakDays = patient.streakDays
                    )
                }
            }
        }
    }

    fun toggleEditMode() {
        _uiState.value = _uiState.value.copy(isEditing = !_uiState.value.isEditing)
    }

    fun updateProfile(
        name: String,
        age: Int,
        caregiverName: String
    ) {
        viewModelScope.launch {
            val updated = PatientEntity(
                id = 1L,
                name = name,
                age = age,
                caregiverName = caregiverName,
                primaryLanguage = _uiState.value.primaryLanguage,
                currentDifficulty = "MEDIUM",
                streakDays = _uiState.value.streakDays,
                lastActiveDate = ""
            )
            patientRepository.insertOrUpdatePatient(updated)
            _uiState.value = _uiState.value.copy(
                name = name,
                age = age,
                caregiverName = caregiverName,
                isEditing = false
            )
        }
    }
}
