package com.example.myapplication.ui.patient.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.GameResultEntity
import com.example.myapplication.data.repository.GameRepository
import com.example.myapplication.data.repository.PatientRepository
import com.example.myapplication.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GameResultUiState(
    val gameResult: Screen.GameResult = Screen.GameResult(),
    val oldDifficulty: String = "Easy",
    val newDifficulty: String = "Easy",
    val adaptiveAction: AdaptiveAction = AdaptiveAction.MAINTAINED,
    val isSaved: Boolean = false
)

enum class AdaptiveAction {
    UPGRADED,
    MAINTAINED,
    LOWERED
}

class GamePlayViewModel(
    private val gameRepository: GameRepository,
    private val patientRepository: PatientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameResultUiState())
    val uiState: StateFlow<GameResultUiState> = _uiState.asStateFlow()

    fun processGameCompletion(result: Screen.GameResult) {
        viewModelScope.launch {
            val currentPatient = patientRepository.getPatientDirect()
            val currentDiff = currentPatient?.currentDifficulty?.ifBlank { "Easy" } ?: result.difficultyLevel

            // Calculate Adaptive Difficulty
            val newDiff: String
            val action: AdaptiveAction

            if (result.score >= 80) {
                newDiff = when (currentDiff.lowercase()) {
                    "easy" -> "Medium"
                    "medium" -> "Hard"
                    else -> "Hard"
                }
                action = if (newDiff != currentDiff) AdaptiveAction.UPGRADED else AdaptiveAction.MAINTAINED
            } else if (result.score < 50) {
                newDiff = when (currentDiff.lowercase()) {
                    "hard" -> "Medium"
                    "medium" -> "Easy"
                    else -> "Easy"
                }
                action = if (newDiff != currentDiff) AdaptiveAction.LOWERED else AdaptiveAction.MAINTAINED
            } else {
                newDiff = currentDiff
                action = AdaptiveAction.MAINTAINED
            }

            // Update patient difficulty in database
            if (newDiff != currentDiff) {
                patientRepository.updateDifficulty(newDiff)
            }

            // Save result entity to Room
            val entity = GameResultEntity(
                gameType = result.gameTitle,
                score = result.score,
                correctCount = result.correctCount,
                incorrectCount = result.incorrectCount,
                timeTakenSeconds = result.timeTakenSeconds,
                difficultyLevel = result.difficultyLevel,
                timestamp = System.currentTimeMillis(),
                cognitiveDomain = result.cognitiveDomain
            )
            gameRepository.saveGameResult(entity)

            _uiState.value = GameResultUiState(
                gameResult = result,
                oldDifficulty = currentDiff,
                newDifficulty = newDiff,
                adaptiveAction = action,
                isSaved = true
            )
        }
    }
}
