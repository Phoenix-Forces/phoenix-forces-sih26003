package com.example.myapplication.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.ArogyaApplication
import com.example.myapplication.ui.caregiver.alerts.CaregiverAlertsViewModel
import com.example.myapplication.ui.caregiver.dashboard.CaregiverViewModel
import com.example.myapplication.ui.caregiver.history.CaregiverGameHistoryViewModel
import com.example.myapplication.ui.caregiver.patient.CaregiverPatientDetailViewModel
import com.example.myapplication.ui.patient.games.GamePlayViewModel
import com.example.myapplication.ui.patient.games.GamesViewModel
import com.example.myapplication.ui.patient.home.PatientHomeViewModel
import com.example.myapplication.ui.patient.language.LanguageViewModel
import com.example.myapplication.ui.patient.profile.ProfileViewModel
import com.example.myapplication.ui.patient.progress.ProgressViewModel
import com.example.myapplication.ui.patient.reminders.RemindersViewModel
import com.example.myapplication.ui.role.RoleViewModel
import com.example.myapplication.ui.voice.VoiceAssistantViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ArogyaApplication)
            RoleViewModel(app.settingsRepository)
        }
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ArogyaApplication)
            PatientHomeViewModel(
                patientRepository = app.patientRepository,
                reminderRepository = app.reminderRepository,
                gameRepository = app.gameRepository,
                settingsRepository = app.settingsRepository
            )
        }
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ArogyaApplication)
            VoiceAssistantViewModel(
                voiceAssistantManager = app.voiceAssistantManager,
                settingsRepository = app.settingsRepository
            )
        }
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ArogyaApplication)
            ProfileViewModel(
                patientRepository = app.patientRepository,
                settingsRepository = app.settingsRepository
            )
        }
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ArogyaApplication)
            LanguageViewModel(
                settingsRepository = app.settingsRepository,
                patientRepository = app.patientRepository
            )
        }
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ArogyaApplication)
            GamesViewModel(
                patientRepository = app.patientRepository
            )
        }
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ArogyaApplication)
            GamePlayViewModel(
                gameRepository = app.gameRepository,
                patientRepository = app.patientRepository
            )
        }
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ArogyaApplication)
            RemindersViewModel(
                reminderRepository = app.reminderRepository
            )
        }
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ArogyaApplication)
            ProgressViewModel(
                gameRepository = app.gameRepository,
                patientRepository = app.patientRepository
            )
        }
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ArogyaApplication)
            CaregiverViewModel(
                patientRepository = app.patientRepository,
                gameRepository = app.gameRepository,
                reminderRepository = app.reminderRepository,
                alertRepository = app.alertRepository
            )
        }
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ArogyaApplication)
            CaregiverPatientDetailViewModel(
                patientRepository = app.patientRepository,
                gameRepository = app.gameRepository,
                reminderRepository = app.reminderRepository
            )
        }
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ArogyaApplication)
            CaregiverGameHistoryViewModel(
                gameRepository = app.gameRepository
            )
        }
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ArogyaApplication)
            CaregiverAlertsViewModel(
                alertRepository = app.alertRepository
            )
        }
    }
}
