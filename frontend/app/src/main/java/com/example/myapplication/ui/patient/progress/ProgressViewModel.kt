package com.example.myapplication.ui.patient.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.GameResultEntity
import com.example.myapplication.data.repository.GameRepository
import com.example.myapplication.data.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class DomainProgressItem(
    val domainName: String,
    val gameTypeKey: String,
    val avgScore: Int,
    val gamesPlayed: Int,
    val bestScore: Int,
    val iconType: String
)

data class DailyTrendPoint(
    val dayLabel: String,
    val dateMillis: Long,
    val avgScore: Int,
    val gamesCount: Int
)

data class ProgressUiState(
    val isLoading: Boolean = true,
    val todayAvgScore: Int = 0,
    val weeklyAvgScore: Int = 0,
    val totalGamesCompleted: Int = 0,
    val bestScore: Int = 0,
    val streakDays: Int = 0,
    val currentDifficulty: String = "MEDIUM",
    val domainScores: List<DomainProgressItem> = emptyList(),
    val dailyTrendPoints: List<DailyTrendPoint> = emptyList()
)

class ProgressViewModel(
    private val gameRepository: GameRepository,
    private val patientRepository: PatientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        loadProgressData()
    }

    private fun loadProgressData() {
        viewModelScope.launch {
            combine(
                gameRepository.allResults,
                patientRepository.patientFlow
            ) { results, patient ->
                val now = System.currentTimeMillis()
                val dayMillis = 86400000L

                // 1. Calculations for summary cards
                val startOfToday = getStartOfDayMillis(now)
                val todayResults = results.filter { it.timestamp >= startOfToday }
                val todayAvgScore = if (todayResults.isNotEmpty()) {
                    todayResults.map { it.score }.average().toInt()
                } else 0

                val sevenDaysAgo = now - (7 * dayMillis)
                val weeklyResults = results.filter { it.timestamp >= sevenDaysAgo }
                val weeklyAvgScore = if (weeklyResults.isNotEmpty()) {
                    weeklyResults.map { it.score }.average().toInt()
                } else if (results.isNotEmpty()) {
                    results.map { it.score }.average().toInt()
                } else 0

                val totalGamesCompleted = results.size
                val bestScore = if (results.isNotEmpty()) results.maxOf { it.score } else 0
                val streakDays = patient?.streakDays ?: 0
                val currentDifficulty = patient?.currentDifficulty ?: "MEDIUM"

                // 2. Domain breakdown for 6 domains
                val requiredDomains = listOf(
                    DomainInfo("Memory Match", "memory_match", "MEMORY"),
                    DomainInfo("Pattern Memory", "pattern_memory", "PATTERN"),
                    DomainInfo("Attention & Focus", "attention_game", "ATTENTION"),
                    DomainInfo("Daily Routine Recall", "daily_routine", "ROUTINE"),
                    DomainInfo("Object Recognition", "object_recognition", "OBJECT"),
                    DomainInfo("Emotion Recognition", "emotion_recognition", "EMOTION")
                )

                val domainProgressItems = requiredDomains.map { domainInfo ->
                    val domainResults = results.filter { result ->
                        isResultForDomain(result, domainInfo.name, domainInfo.gameKey)
                    }
                    val avg = if (domainResults.isNotEmpty()) domainResults.map { it.score }.average().toInt() else 0
                    val count = domainResults.size
                    val max = if (domainResults.isNotEmpty()) domainResults.maxOf { it.score } else 0

                    DomainProgressItem(
                        domainName = domainInfo.name,
                        gameTypeKey = domainInfo.gameKey,
                        avgScore = avg,
                        gamesPlayed = count,
                        bestScore = max,
                        iconType = domainInfo.iconType
                    )
                }

                // 3. Weekly Score Trend (Last 7 Days)
                val trendPoints = mutableListOf<DailyTrendPoint>()
                val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
                val calendar = Calendar.getInstance()

                for (i in 6 downTo 0) {
                    calendar.timeInMillis = now - (i * dayMillis)
                    val dayStart = getStartOfDayMillis(calendar.timeInMillis)
                    val dayEnd = dayStart + dayMillis - 1
                    val dayLabel = dayFormat.format(Date(calendar.timeInMillis))

                    val dayResults = results.filter { it.timestamp in dayStart..dayEnd }
                    val dayAvg = if (dayResults.isNotEmpty()) dayResults.map { it.score }.average().toInt() else 0

                    trendPoints.add(
                        DailyTrendPoint(
                            dayLabel = dayLabel,
                            dateMillis = calendar.timeInMillis,
                            avgScore = dayAvg,
                            gamesCount = dayResults.size
                        )
                    )
                }

                ProgressUiState(
                    isLoading = false,
                    todayAvgScore = todayAvgScore,
                    weeklyAvgScore = weeklyAvgScore,
                    totalGamesCompleted = totalGamesCompleted,
                    bestScore = bestScore,
                    streakDays = streakDays,
                    currentDifficulty = currentDifficulty,
                    domainScores = domainProgressItems,
                    dailyTrendPoints = trendPoints
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    private fun isResultForDomain(result: GameResultEntity, domainName: String, gameKey: String): Boolean {
        val rDomain = result.cognitiveDomain.lowercase()
        val rType = result.gameType.lowercase()
        val targetName = domainName.lowercase()
        val targetKey = gameKey.lowercase()

        return rDomain.contains(targetName) || rDomain.contains(targetKey) ||
                rType.contains(targetName) || rType.contains(targetKey) ||
                (targetKey == "memory_match" && (rType.contains("flip") || rType.contains("match"))) ||
                (targetKey == "attention_game" && (rType.contains("focus") || rType.contains("attention"))) ||
                (targetKey == "daily_routine" && (rType.contains("schedule") || rType.contains("routine"))) ||
                (targetKey == "object_recognition" && (rType.contains("item") || rType.contains("object"))) ||
                (targetKey == "emotion_recognition" && (rType.contains("feeling") || rType.contains("emotion")))
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

    private data class DomainInfo(val name: String, val gameKey: String, val iconType: String)
}
