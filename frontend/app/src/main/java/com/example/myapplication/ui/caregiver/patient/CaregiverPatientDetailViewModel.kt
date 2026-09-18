package com.example.myapplication.ui.caregiver.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.PatientEntity
import com.example.myapplication.data.repository.GameRepository
import com.example.myapplication.data.repository.PatientRepository
import com.example.myapplication.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TimelineEvent(
    val timeStr: String,
    val title: String,
    val category: String, // GAME, MEDICATION, HYDRATION, APPOINTMENT
    val statusText: String,
    val isSuccess: Boolean
)

data class PatientDetailUiState(
    val isLoading: Boolean = true,
    val patient: PatientEntity? = null,
    val cognitiveHealthIndex: Int = 85,
    val strongestDomain: String = "Daily Routine Recall",
    val domainNeedingFocus: String = "Attention & Focus",
    val weeklyCompletionRate: Int = 88,
    val currentDifficulty: String = "MEDIUM",
    val primaryLanguage: String = "en",
    val emergencyContactCaregiver: String = "+91 98765 43210",
    val emergencyContactDoctor: String = "+91 98123 45678",
    val timelineEvents: List<TimelineEvent> = emptyList(),
    val difficultyHistory: List<String> = listOf(
        "Jan 2025: Initial Evaluation at EASY level",
        "Feb 2025: Promoted to MEDIUM level (Score > 80% consistently)"
    )
)

class CaregiverPatientDetailViewModel(
    private val patientRepository: PatientRepository,
    private val gameRepository: GameRepository,
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientDetailUiState())
    val uiState: StateFlow<PatientDetailUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                patientRepository.patientFlow,
                gameRepository.allResults,
                reminderRepository.allReminders
            ) { patient, gameResults, reminders ->
                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

                val events = mutableListOf<TimelineEvent>()

                // Add reminders to timeline
                reminders.forEach { r ->
                    events.add(
                        TimelineEvent(
                            timeStr = r.timeString,
                            title = r.title,
                            category = r.category.uppercase(),
                            statusText = if (r.isEnabled) "Scheduled / Active" else "Completed",
                            isSuccess = r.isEnabled
                        )
                    )
                }

                // Add recent game sessions to timeline
                gameResults.take(3).forEach { g ->
                    events.add(
                        TimelineEvent(
                            timeStr = timeFormat.format(Date(g.timestamp)),
                            title = "${g.gameType} Game",
                            category = "GAME",
                            statusText = "Score: ${g.score}%",
                            isSuccess = g.score >= 70
                        )
                    )
                }

                val currentDiff = patient?.currentDifficulty ?: "MEDIUM"
                val currentLang = patient?.primaryLanguage ?: "en"

                PatientDetailUiState(
                    isLoading = false,
                    patient = patient,
                    cognitiveHealthIndex = if (gameResults.isNotEmpty()) gameResults.map { it.score }.average().toInt() else 85,
                    strongestDomain = "Daily Routine Recall",
                    domainNeedingFocus = "Attention & Focus",
                    weeklyCompletionRate = 88,
                    currentDifficulty = currentDiff,
                    primaryLanguage = currentLang,
                    timelineEvents = events.sortedBy { it.timeStr },
                    difficultyHistory = listOf(
                        "Jan 2025: Assessed at EASY Level",
                        "Feb 2025: Auto-adapted to $currentDiff Level based on 5-day streak"
                    )
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun updateDifficulty(difficulty: String) {
        viewModelScope.launch {
            patientRepository.updateDifficulty(difficulty)
        }
    }

    fun updateLanguage(languageCode: String) {
        viewModelScope.launch {
            patientRepository.updateLanguage(languageCode)
        }
    }

    fun updatePatientBio(name: String, age: Int, caregiverName: String) {
        viewModelScope.launch {
            val current = patientRepository.getPatientDirect() ?: PatientEntity(
                id = 1L,
                name = name,
                age = age,
                caregiverName = caregiverName,
                primaryLanguage = _uiState.value.primaryLanguage,
                currentDifficulty = _uiState.value.currentDifficulty,
                streakDays = 5,
                lastActiveDate = ""
            )
            val updated = current.copy(
                name = name,
                age = age,
                caregiverName = caregiverName
            )
            patientRepository.insertOrUpdatePatient(updated)
        }
    }
}
