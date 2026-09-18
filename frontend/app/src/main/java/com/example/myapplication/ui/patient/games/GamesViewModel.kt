package com.example.myapplication.ui.patient.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.PatientRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class GamesUiState(
    val currentDifficulty: String = "Easy",
    val patientName: String = "Patient"
)

class GamesViewModel(
    patientRepository: PatientRepository
) : ViewModel() {

    val uiState: StateFlow<GamesUiState> = patientRepository.patientFlow.map { patient ->
        GamesUiState(
            currentDifficulty = patient?.currentDifficulty?.ifBlank { "Easy" } ?: "Easy",
            patientName = patient?.name ?: "Patient"
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GamesUiState()
    )
}
