package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.myapplication.ArogyaApplication
import com.example.myapplication.ui.AppViewModelProvider
import com.example.myapplication.ui.auth.LoginScreen
import com.example.myapplication.ui.auth.LoginViewModel
import com.example.myapplication.ui.auth.RegisterScreen
import com.example.myapplication.ui.auth.RegisterViewModel
import com.example.myapplication.ui.auth.VerificationScreen
import com.example.myapplication.ui.caregiver.alerts.CaregiverAlertsScreen
import com.example.myapplication.ui.caregiver.alerts.CaregiverAlertsViewModel
import com.example.myapplication.ui.caregiver.dashboard.CaregiverDashboardScreen
import com.example.myapplication.ui.caregiver.dashboard.CaregiverViewModel
import com.example.myapplication.ui.caregiver.history.CaregiverGameHistoryScreen
import com.example.myapplication.ui.caregiver.history.CaregiverGameHistoryViewModel
import com.example.myapplication.ui.caregiver.patient.CaregiverPatientDetailScreen
import com.example.myapplication.ui.caregiver.patient.CaregiverPatientDetailViewModel
import com.example.myapplication.ui.patient.games.GamePlayViewModel
import com.example.myapplication.ui.patient.games.GameResultScreen
import com.example.myapplication.ui.patient.games.GamesListScreen
import com.example.myapplication.ui.patient.games.GamesViewModel
import com.example.myapplication.ui.patient.games.play.AttentionGameScreen
import com.example.myapplication.ui.patient.games.play.DailyRoutineGameScreen
import com.example.myapplication.ui.patient.games.play.EmotionRecognitionGameScreen
import com.example.myapplication.ui.patient.games.play.MemoryMatchScreen
import com.example.myapplication.ui.patient.games.play.ObjectRecognitionGameScreen
import com.example.myapplication.ui.patient.games.play.PatternMemoryScreen
import com.example.myapplication.ui.patient.home.PatientHomeScreen
import com.example.myapplication.ui.patient.home.PatientHomeViewModel
import com.example.myapplication.ui.patient.language.LanguageSettingsScreen
import com.example.myapplication.ui.patient.language.LanguageViewModel
import com.example.myapplication.ui.patient.profile.ProfileScreen
import com.example.myapplication.ui.patient.profile.ProfileViewModel
import com.example.myapplication.ui.patient.progress.PatientProgressScreen
import com.example.myapplication.ui.patient.progress.ProgressViewModel
import com.example.myapplication.ui.patient.reminders.RemindersScreen
import com.example.myapplication.ui.patient.reminders.RemindersViewModel
import com.example.myapplication.ui.role.RoleSelectionScreen
import com.example.myapplication.ui.splash.SplashScreen
import com.example.myapplication.ui.voice.VoiceAssistantScreen
import com.example.myapplication.ui.voice.VoiceAssistantViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val app = context.applicationContext as ArogyaApplication
    val backStack = rememberNavBackStack(Screen.Splash)

    NavDisplay(
        backStack = backStack,
        modifier = modifier
    ) { key ->
        NavEntry(key) {
            when (key) {
                is Screen.Splash -> {
                    SplashScreen(
                        onTimeout = {
                            if (backStack.isNotEmpty()) backStack.removeAt(backStack.size - 1)
                            when {
                                // 1. Authenticated or Demo session: route by role
                                app.authRepository.isAuthenticated() || app.authRepository.isDemoMode() -> {
                                    val role = app.authRepository.getUserRole()
                                    if (role == "caregiver") {
                                        backStack.add(Screen.CaregiverDashboard)
                                    } else {
                                        backStack.add(Screen.PatientHome)
                                    }
                                }
                                // 2. Not authenticated: show login
                                else -> {
                                    backStack.add(Screen.Login)
                                }
                            }
                        }
                    )
                }
                is Screen.Login -> {
                    val loginViewModel: LoginViewModel = viewModel(
                        factory = AppViewModelProvider.Factory
                    )

                    LaunchedEffect(Unit) {
                        loginViewModel.reset()
                    }

                    val loginUiState by loginViewModel.uiState.collectAsState()

                    LaunchedEffect(loginUiState.loginSuccess) {
                        if (loginUiState.loginSuccess) {
                            backStack.clear()
                            if (loginUiState.role == "caregiver") {
                                backStack.add(Screen.CaregiverDashboard)
                            } else {
                                backStack.add(Screen.PatientHome)
                            }
                        }
                    }

                    LoginScreen(
                        uiState = loginUiState,
                        onSignIn = { username, password ->
                            loginViewModel.signIn(username, password)
                        },
                        onCreateAccount = { backStack.add(Screen.RoleSelection()) },
                        onDemoMode = { backStack.add(Screen.RoleSelection("demo")) },
                        onClearError = { loginViewModel.clearError() }
                    )
                }
                is Screen.RoleSelection -> {
                    RoleSelectionScreen(
                        onSelectRole = { role ->
                            if (key.reason == "demo") {
                                app.authRepository.enterDemoMode(role.lowercase())
                                backStack.clear()
                                if (role.equals("CAREGIVER", ignoreCase = true)) {
                                    backStack.add(Screen.CaregiverDashboard)
                                } else {
                                    backStack.add(Screen.PatientHome)
                                }
                            } else {
                                backStack.add(Screen.Register(role.lowercase()))
                            }
                        }
                    )
                }
                is Screen.Register -> {
                    val registerViewModel: RegisterViewModel = viewModel(
                        factory = AppViewModelProvider.Factory
                    )
                    RegisterScreen(
                        role = key.role,
                        viewModel = registerViewModel,
                        onNavigateToVerification = { backStack.add(Screen.Verification) },
                        onNavigateBack = { if (backStack.isNotEmpty()) backStack.removeAt(backStack.size - 1) }
                    )
                }
                is Screen.Verification -> {
                    val registerViewModel: RegisterViewModel = viewModel(
                        factory = AppViewModelProvider.Factory
                    )
                    VerificationScreen(
                        viewModel = registerViewModel,
                        onNavigateToLogin = {
                            backStack.clear()
                            backStack.add(Screen.Login)
                        },
                        onNavigateBack = { if (backStack.isNotEmpty()) backStack.removeAt(backStack.size - 1) }
                    )
                }
                is Screen.PatientHome -> {
                    val viewModel: PatientHomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val uiState by viewModel.uiState.collectAsState()

                    PatientHomeScreen(
                        uiState = uiState,
                        onStartGameClicked = { backStack.add(Screen.GamesList) },
                        onVoiceAssistantClicked = { backStack.add(Screen.VoiceAssistant) },
                        onLanguageSelected = { lang -> viewModel.changeLanguage(lang) },
                        onNavigateToProfile = { backStack.add(Screen.Profile) },
                        onNavigateToGames = { backStack.add(Screen.GamesList) },
                        onNavigateToReminders = { backStack.add(Screen.Reminders) },
                        onNavigateToProgress = { backStack.add(Screen.PatientProgress) },
                        onNavigateToLanguage = { backStack.add(Screen.LanguageSettings) },
                        onSwitchRole = {
                            app.authRepository.signOut()
                            backStack.clear()
                            backStack.add(Screen.Login)
                        }
                    )
                }
                is Screen.Profile -> {
                    val viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val uiState by viewModel.uiState.collectAsState()

                    ProfileScreen(
                        uiState = uiState,
                        onToggleEditMode = { viewModel.toggleEditMode() },
                        onSaveProfile = { name, age, caregiver ->
                            viewModel.updateProfile(name, age, caregiver)
                        },
                        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                    )
                }
                is Screen.LanguageSettings -> {
                    val viewModel: LanguageViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val uiState by viewModel.uiState.collectAsState()

                    LanguageSettingsScreen(
                        uiState = uiState,
                        onLanguageSelected = { lang -> viewModel.selectLanguage(lang) },
                        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                    )
                }
                is Screen.VoiceAssistant -> {
                    val viewModel: VoiceAssistantViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val uiState by viewModel.uiState.collectAsState()

                    VoiceAssistantScreen(
                        uiState = uiState,
                        onStartListening = {
                            viewModel.startListening(
                                onNavigateToGame = {
                                    if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                                    backStack.add(Screen.GamePlay())
                                },
                                onNavigateToGamesList = {
                                    if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                                    backStack.add(Screen.GamesList)
                                },
                                onNavigateToProgress = {
                                    if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                                    backStack.add(Screen.PatientProgress)
                                },
                                onNavigateToReminders = {
                                    if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                                    backStack.add(Screen.Reminders)
                                },
                                onNavigateToHome = {
                                    if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                                    backStack.add(Screen.PatientHome)
                                }
                            )
                        },
                        onStopListening = { viewModel.stopListening() },
                        onQuickCommand = { cmd ->
                            viewModel.processCommand(
                                command = cmd,
                                onNavigateToGame = {
                                    if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                                    backStack.add(Screen.GamePlay())
                                },
                                onNavigateToGamesList = {
                                    if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                                    backStack.add(Screen.GamesList)
                                },
                                onNavigateToProgress = {
                                    if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                                    backStack.add(Screen.PatientProgress)
                                },
                                onNavigateToReminders = {
                                    if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                                    backStack.add(Screen.Reminders)
                                },
                                onNavigateToHome = {
                                    if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                                    backStack.add(Screen.PatientHome)
                                }
                            )
                        },
                        onClose = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                    )
                }
                is Screen.GamesList -> {
                    val viewModel: GamesViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val uiState by viewModel.uiState.collectAsState()

                    GamesListScreen(
                        uiState = uiState,
                        onGameSelected = { gameId ->
                            backStack.add(Screen.GamePlay(gameId))
                        },
                        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                    )
                }
                is Screen.GamePlay -> {
                    val gamesViewModel: GamesViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val gamesUiState by gamesViewModel.uiState.collectAsState()
                    val difficulty = gamesUiState.currentDifficulty

                    when (key.gameId) {
                        "memory_flip", "memory_match" -> {
                            MemoryMatchScreen(
                                difficulty = difficulty,
                                onGameFinished = { result -> backStack.add(result) },
                                onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                            )
                        }
                        "pattern_memory" -> {
                            PatternMemoryScreen(
                                difficulty = difficulty,
                                onGameFinished = { result -> backStack.add(result) },
                                onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                            )
                        }
                        "attention_game" -> {
                            AttentionGameScreen(
                                difficulty = difficulty,
                                onGameFinished = { result -> backStack.add(result) },
                                onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                            )
                        }
                        "daily_routine" -> {
                            DailyRoutineGameScreen(
                                difficulty = difficulty,
                                onGameFinished = { result -> backStack.add(result) },
                                onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                            )
                        }
                        "object_recognition" -> {
                            ObjectRecognitionGameScreen(
                                difficulty = difficulty,
                                onGameFinished = { result -> backStack.add(result) },
                                onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                            )
                        }
                        "emotion_recognition" -> {
                            EmotionRecognitionGameScreen(
                                difficulty = difficulty,
                                onGameFinished = { result -> backStack.add(result) },
                                onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                            )
                        }
                        else -> {
                            MemoryMatchScreen(
                                difficulty = difficulty,
                                onGameFinished = { result -> backStack.add(result) },
                                onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                            )
                        }
                    }
                }
                is Screen.GameResult -> {
                    val gamePlayViewModel: GamePlayViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val uiState by gamePlayViewModel.uiState.collectAsState()

                    LaunchedEffect(key) {
                        gamePlayViewModel.processGameCompletion(key)
                    }

                    GameResultScreen(
                        uiState = uiState,
                        onPlayAgain = {
                            if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                            backStack.add(Screen.GamePlay(key.gameId))
                        },
                        onBackToGames = {
                            if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                            if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                            backStack.add(Screen.GamesList)
                        },
                        onViewProgress = {
                            if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                            if (backStack.size > 1) backStack.removeAt(backStack.size - 1)
                            backStack.add(Screen.PatientProgress)
                        }
                    )
                }
                is Screen.Reminders -> {
                    val viewModel: RemindersViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val uiState by viewModel.uiState.collectAsState()

                    RemindersScreen(
                        uiState = uiState,
                        onFilterCategory = { cat -> viewModel.filterByCategory(cat) },
                        onAddReminder = { title, cat, timeStr, rep, notes, context ->
                            viewModel.addReminder(title, cat, timeStr, rep, notes, context)
                        },
                        onUpdateReminder = { reminder, context ->
                            viewModel.updateReminder(reminder, context)
                        },
                        onToggleReminder = { reminder, isEnabled, context ->
                            viewModel.toggleReminderStatus(reminder, isEnabled, context)
                        },
                        onDeleteReminder = { reminder, context ->
                            viewModel.deleteReminder(reminder, context)
                        },
                        onTestNotification = { context ->
                            viewModel.triggerTestNotification(context)
                        },
                        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                    )
                }
                is Screen.PatientProgress -> {
                    val viewModel: ProgressViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val uiState by viewModel.uiState.collectAsState()

                    PatientProgressScreen(
                        uiState = uiState,
                        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                    )
                }
                is Screen.CaregiverDashboard -> {
                    val viewModel: CaregiverViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val uiState by viewModel.uiState.collectAsState()

                    CaregiverDashboardScreen(
                        uiState = uiState,
                        onNavigateToGameHistory = { backStack.add(Screen.CaregiverGameHistory) },
                        onNavigateToAnalytics = { backStack.add(Screen.PatientProgress) },
                        onNavigateToReminders = { backStack.add(Screen.Reminders) },
                        onNavigateToPatientDetail = { backStack.add(Screen.CaregiverPatientDetail()) },
                        onNavigateToAlerts = { backStack.add(Screen.CaregiverAlerts) },
                        onBackToRoleSelection = {
                            app.authRepository.signOut()
                            backStack.clear()
                            backStack.add(Screen.Login)
                        }
                    )
                }
                is Screen.CaregiverPatientDetail -> {
                    val viewModel: CaregiverPatientDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val uiState by viewModel.uiState.collectAsState()

                    CaregiverPatientDetailScreen(
                        uiState = uiState,
                        onDifficultyChanged = { difficulty -> viewModel.updateDifficulty(difficulty) },
                        onLanguageChanged = { languageCode -> viewModel.updateLanguage(languageCode) },
                        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                    )
                }
                is Screen.CaregiverGameHistory -> {
                    val viewModel: CaregiverGameHistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val uiState by viewModel.uiState.collectAsState()

                    CaregiverGameHistoryScreen(
                        uiState = uiState,
                        onDomainFilterSelected = { domain -> viewModel.setDomainFilter(domain) },
                        onSortBySelected = { sort -> viewModel.setSortBy(sort) },
                        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                    )
                }
                is Screen.CaregiverAlerts -> {
                    val viewModel: CaregiverAlertsViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val uiState by viewModel.uiState.collectAsState()

                    CaregiverAlertsScreen(
                        uiState = uiState,
                        onFilterSelected = { filter -> viewModel.setFilter(filter) },
                        onMarkAsRead = { alertId -> viewModel.markAsRead(alertId) },
                        onMarkAllAsRead = { viewModel.markAllAsRead() },
                        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                    )
                }
            }
        }
    }
}
