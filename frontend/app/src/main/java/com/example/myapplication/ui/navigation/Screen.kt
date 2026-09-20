package com.example.myapplication.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {
    @Serializable
    data object Login : Screen

    @Serializable
    data object Splash : Screen

    @Serializable
    data class RoleSelection(val reason: String = "register") : Screen

    @Serializable
    data class Register(val role: String = "patient") : Screen

    @Serializable
    data object Verification : Screen

    @Serializable
    data object PatientHome : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    data object LanguageSettings : Screen

    @Serializable
    data object VoiceAssistant : Screen

    @Serializable
    data object GamesList : Screen

    @Serializable
    data class GamePlay(val gameId: String = "memory_flip") : Screen

    @Serializable
    data class GameResult(
        val gameId: String = "memory_flip",
        val gameTitle: String = "Cognitive Game",
        val score: Int = 0,
        val correctCount: Int = 0,
        val incorrectCount: Int = 0,
        val timeTakenSeconds: Int = 0,
        val difficultyLevel: String = "Easy",
        val cognitiveDomain: String = "Memory"
    ) : Screen

    @Serializable
    data object Reminders : Screen

    @Serializable
    data object PatientProgress : Screen

    @Serializable
    data object CaregiverDashboard : Screen

    @Serializable
    data class CaregiverPatientDetail(val patientId: String = "1") : Screen

    @Serializable
    data object CaregiverGameHistory : Screen

    @Serializable
    data object CaregiverAlerts : Screen
}
