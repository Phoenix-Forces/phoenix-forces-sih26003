package com.example.myapplication.ui.patient.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.data.repository.GameRepository
import com.example.myapplication.data.repository.PatientRepository
import com.example.myapplication.data.repository.ReminderRepository
import com.example.myapplication.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class PatientHomeUiState(
    val patientName: String = "Ramesh Kumar",
    val greetingResId: Int = R.string.greeting_morning,
    val currentDateString: String = "",
    val streakDays: Int = 5,
    val weeklyAverageScore: Int = 85,
    val nextReminderTitle: String? = null,
    val nextReminderTime: String? = null,
    val currentLanguage: String = "en",
    val isLoading: Boolean = false
)

class PatientHomeViewModel(
    private val patientRepository: PatientRepository,
    private val reminderRepository: ReminderRepository,
    private val gameRepository: GameRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientHomeUiState())
    val uiState: StateFlow<PatientHomeUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                patientRepository.patientFlow,
                reminderRepository.activeReminders,
                gameRepository.allResults,
                settingsRepository.languageFlow
            ) { patient, reminders, results, language ->
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val greetingRes = when (hour) {
                    in 5..11 -> R.string.greeting_morning
                    in 12..16 -> R.string.greeting_afternoon
                    else -> R.string.greeting_evening
                }

                val locale = when (language) {
                    "as" -> Locale.forLanguageTag("as-IN")
                    "bn" -> Locale.forLanguageTag("bn-IN")
                    "hi" -> Locale.forLanguageTag("hi-IN")
                    else -> Locale.ENGLISH
                }
                val dateFormat = SimpleDateFormat("EEEE, d MMMM", locale)
                val formattedDate = dateFormat.format(Date())

                val nextReminder = reminders.firstOrNull()

                val avgScore = if (results.isNotEmpty()) {
                    results.map { it.score }.average().toInt()
                } else {
                    85
                }

                PatientHomeUiState(
                    patientName = patient?.name ?: "Ramesh Kumar",
                    greetingResId = greetingRes,
                    currentDateString = formattedDate,
                    streakDays = patient?.streakDays ?: 5,
                    weeklyAverageScore = avgScore,
                    nextReminderTitle = nextReminder?.title,
                    nextReminderTime = nextReminder?.timeString,
                    currentLanguage = language,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun changeLanguage(languageCode: String) {
        viewModelScope.launch {
            settingsRepository.setLanguage(languageCode)
            patientRepository.updateLanguage(languageCode)
        }
    }
}
