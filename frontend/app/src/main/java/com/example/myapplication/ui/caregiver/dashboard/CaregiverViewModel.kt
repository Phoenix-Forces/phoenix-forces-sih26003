package com.example.myapplication.ui.caregiver.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.AlertEntity
import com.example.myapplication.data.local.entity.GameResultEntity
import com.example.myapplication.data.local.entity.PatientEntity
import com.example.myapplication.data.repository.AlertRepository
import com.example.myapplication.data.repository.GameRepository
import com.example.myapplication.data.repository.PatientRepository
import com.example.myapplication.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar

data class CaregiverUiState(
    val isLoading: Boolean = true,
    val patient: PatientEntity? = null,
    val patientName: String = "Ramesh Kumar",
    val patientAge: Int = 72,
    val patientDifficulty: String = "MEDIUM",
    val caregiverName: String = "Sunita Kumar",
    val todaysGamesCount: Int = 0,
    val todayAvgScore: Int = 0,
    val remindersCompletedCount: Int = 3,
    val totalRemindersCount: Int = 4,
    val unreadAlertsCount: Int = 0,
    val unreadAlerts: List<AlertEntity> = emptyList(),
    val recentGameResults: List<GameResultEntity> = emptyList()
)

class CaregiverViewModel(
    private val patientRepository: PatientRepository,
    private val gameRepository: GameRepository,
    private val reminderRepository: ReminderRepository,
    private val alertRepository: AlertRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CaregiverUiState())
    val uiState: StateFlow<CaregiverUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            combine(
                patientRepository.patientFlow,
                gameRepository.allResults,
                reminderRepository.allReminders,
                alertRepository.allAlerts
            ) { patient, results, reminders, alerts ->
                val now = System.currentTimeMillis()
                val startOfToday = getStartOfDayMillis(now)

                val todayResults = results.filter { it.timestamp >= startOfToday }
                val todaysGamesCount = todayResults.size
                val todayAvgScore = if (todayResults.isNotEmpty()) {
                    todayResults.map { it.score }.average().toInt()
                } else if (results.isNotEmpty()) {
                    results.take(3).map { it.score }.average().toInt()
                } else 0

                val activeReminders = reminders.filter { it.isEnabled }
                val totalRemindersCount = reminders.size
                val remindersCompletedCount = activeReminders.size

                val unread = alerts.filter { !it.isRead }
                val recentGames = results.take(5)

                CaregiverUiState(
                    isLoading = false,
                    patient = patient,
                    patientName = patient?.name ?: "Ramesh Kumar",
                    patientAge = patient?.age ?: 72,
                    patientDifficulty = patient?.currentDifficulty ?: "MEDIUM",
                    caregiverName = patient?.caregiverName?.takeIf { it.isNotBlank() } ?: "Sunita Kumar",
                    todaysGamesCount = todaysGamesCount,
                    todayAvgScore = todayAvgScore,
                    remindersCompletedCount = remindersCompletedCount,
                    totalRemindersCount = totalRemindersCount,
                    unreadAlertsCount = unread.size,
                    unreadAlerts = unread.take(3),
                    recentGameResults = recentGames
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun markAlertAsRead(alertId: Long) {
        viewModelScope.launch {
            alertRepository.markAsRead(alertId)
        }
    }

    private fun getStartOfDayMillis(timeMillis: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeMillis
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
