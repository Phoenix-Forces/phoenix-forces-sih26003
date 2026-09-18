package com.example.myapplication.ui.voice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.SettingsRepository
import com.example.myapplication.utils.VoiceAssistantManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class VoiceState {
    IDLE, LISTENING, PROCESSING, SPOKEN
}

data class VoiceAssistantUiState(
    val voiceState: VoiceState = VoiceState.IDLE,
    val recognizedText: String = "",
    val assistantResponse: String = "",
    val languageCode: String = "en"
)

class VoiceAssistantViewModel(
    private val voiceAssistantManager: VoiceAssistantManager,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VoiceAssistantUiState())
    val uiState: StateFlow<VoiceAssistantUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val lang = settingsRepository.languageFlow.first()
            _uiState.value = _uiState.value.copy(languageCode = lang)
        }
    }

    fun startListening(
        onNavigateToGame: () -> Unit,
        onNavigateToGamesList: () -> Unit,
        onNavigateToProgress: () -> Unit,
        onNavigateToReminders: () -> Unit,
        onNavigateToHome: () -> Unit
    ) {
        _uiState.value = _uiState.value.copy(
            voiceState = VoiceState.LISTENING,
            recognizedText = "",
            assistantResponse = ""
        )

        voiceAssistantManager.startListening(
            languageCode = _uiState.value.languageCode,
            onResult = { result ->
                processCommand(
                    command = result,
                    onNavigateToGame = onNavigateToGame,
                    onNavigateToGamesList = onNavigateToGamesList,
                    onNavigateToProgress = onNavigateToProgress,
                    onNavigateToReminders = onNavigateToReminders,
                    onNavigateToHome = onNavigateToHome
                )
            },
            onError = { error ->
                _uiState.value = _uiState.value.copy(
                    voiceState = VoiceState.IDLE,
                    assistantResponse = error
                )
                voiceAssistantManager.speak(error, _uiState.value.languageCode)
            }
        )
    }

    fun stopListening() {
        voiceAssistantManager.stopListening()
        _uiState.value = _uiState.value.copy(voiceState = VoiceState.IDLE)
    }

    fun processCommand(
        command: String,
        onNavigateToGame: () -> Unit,
        onNavigateToGamesList: () -> Unit,
        onNavigateToProgress: () -> Unit,
        onNavigateToReminders: () -> Unit,
        onNavigateToHome: () -> Unit
    ) {
        _uiState.value = _uiState.value.copy(
            voiceState = VoiceState.PROCESSING,
            recognizedText = command
        )

        val lowerCmd = command.lowercase()
        val lang = _uiState.value.languageCode

        when {
            lowerCmd.contains("game") || lowerCmd.contains("play") || lowerCmd.contains("গেম") || lowerCmd.contains("খেলা") || lowerCmd.contains("गेम") -> {
                val response = "Opening cognitive game now."
                _uiState.value = _uiState.value.copy(
                    voiceState = VoiceState.SPOKEN,
                    assistantResponse = response
                )
                voiceAssistantManager.speak(response, lang)
                onNavigateToGame()
            }
            lowerCmd.contains("progress") || lowerCmd.contains("score") || lowerCmd.contains("অগ্ৰগতি") || lowerCmd.contains("অগ্রগতি") || lowerCmd.contains("प्रगति") -> {
                val response = "Opening your progress summary."
                _uiState.value = _uiState.value.copy(
                    voiceState = VoiceState.SPOKEN,
                    assistantResponse = response
                )
                voiceAssistantManager.speak(response, lang)
                onNavigateToProgress()
            }
            lowerCmd.contains("reminder") || lowerCmd.contains("medicine") || lowerCmd.contains("সোঁৱৰণী") || lowerCmd.contains("অনুস্মারক") || lowerCmd.contains("रिमाइंडर") -> {
                val response = "Opening your daily reminders."
                _uiState.value = _uiState.value.copy(
                    voiceState = VoiceState.SPOKEN,
                    assistantResponse = response
                )
                voiceAssistantManager.speak(response, lang)
                onNavigateToReminders()
            }
            lowerCmd.contains("home") || lowerCmd.contains("হোম") || lowerCmd.contains("होम") -> {
                val response = "Navigating to Home screen."
                _uiState.value = _uiState.value.copy(
                    voiceState = VoiceState.SPOKEN,
                    assistantResponse = response
                )
                voiceAssistantManager.speak(response, lang)
                onNavigateToHome()
            }
            else -> {
                val response = "Sorry, I didn't understand that. Try saying 'Start game' or 'Show reminders'."
                _uiState.value = _uiState.value.copy(
                    voiceState = VoiceState.IDLE,
                    assistantResponse = response
                )
                voiceAssistantManager.speak(response, lang)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceAssistantManager.stopListening()
    }
}
