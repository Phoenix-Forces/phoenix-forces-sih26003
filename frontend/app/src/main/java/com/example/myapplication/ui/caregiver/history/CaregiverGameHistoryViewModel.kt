package com.example.myapplication.ui.caregiver.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.GameResultEntity
import com.example.myapplication.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GameHistoryUiState(
    val isLoading: Boolean = true,
    val selectedDomainFilter: String = "ALL",
    val sortBy: String = "NEWEST",
    val allResults: List<GameResultEntity> = emptyList(),
    val filteredResults: List<GameResultEntity> = emptyList(),
    val totalSessions: Int = 0,
    val overallAvgScore: Int = 0,
    val totalTimeSpentMinutes: Int = 0
)

class CaregiverGameHistoryViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameHistoryUiState())
    val uiState: StateFlow<GameHistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            gameRepository.allResults.collect { results ->
                val totalSessions = results.size
                val overallAvg = if (results.isNotEmpty()) results.map { it.score }.average().toInt() else 0
                val totalSecs = results.sumOf { it.timeTakenSeconds }
                val totalMins = (totalSecs / 60)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    allResults = results,
                    totalSessions = totalSessions,
                    overallAvgScore = overallAvg,
                    totalTimeSpentMinutes = totalMins
                )
                applyFilterAndSort()
            }
        }
    }

    fun setDomainFilter(domain: String) {
        _uiState.value = _uiState.value.copy(selectedDomainFilter = domain)
        applyFilterAndSort()
    }

    fun setSortBy(sort: String) {
        _uiState.value = _uiState.value.copy(sortBy = sort)
        applyFilterAndSort()
    }

    private fun applyFilterAndSort() {
        val current = _uiState.value
        var filtered = current.allResults

        // Filter by domain
        if (current.selectedDomainFilter != "ALL") {
            filtered = filtered.filter { result ->
                result.cognitiveDomain.contains(current.selectedDomainFilter, ignoreCase = true) ||
                        result.gameType.contains(current.selectedDomainFilter, ignoreCase = true)
            }
        }

        // Sort
        filtered = when (current.sortBy) {
            "OLDEST" -> filtered.sortedBy { it.timestamp }
            "HIGHEST_SCORE" -> filtered.sortedByDescending { it.score }
            "LOWEST_SCORE" -> filtered.sortedBy { it.score }
            else -> filtered.sortedByDescending { it.timestamp } // NEWEST
        }

        _uiState.value = current.copy(filteredResults = filtered)
    }
}
