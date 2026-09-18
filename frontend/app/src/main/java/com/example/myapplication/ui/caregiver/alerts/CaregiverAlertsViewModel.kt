package com.example.myapplication.ui.caregiver.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.AlertEntity
import com.example.myapplication.data.repository.AlertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AlertsUiState(
    val isLoading: Boolean = true,
    val selectedFilter: String = "ALL", // ALL, UNREAD, HIGH
    val allAlerts: List<AlertEntity> = emptyList(),
    val filteredAlerts: List<AlertEntity> = emptyList(),
    val unreadCount: Int = 0
)

class CaregiverAlertsViewModel(
    private val alertRepository: AlertRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlertsUiState())
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    init {
        loadAlerts()
    }

    private fun loadAlerts() {
        viewModelScope.launch {
            alertRepository.allAlerts.collect { alerts ->
                val listToDisplay = if (alerts.isEmpty()) {
                    // Provide required sample alerts if database has no entries
                    val now = System.currentTimeMillis()
                    listOf(
                        AlertEntity(
                            id = 101L,
                            title = "No cognitive activity recorded yesterday",
                            message = "Ramesh did not play any cognitive training games yesterday.",
                            severity = "HIGH",
                            timestamp = now - (3600000L * 20),
                            isRead = false
                        ),
                        AlertEntity(
                            id = 102L,
                            title = "Hydration reminder missed at 11:00 AM",
                            message = "11:00 AM water intake check-in was not confirmed by patient.",
                            severity = "WARNING",
                            timestamp = now - (3600000L * 4),
                            isRead = false
                        ),
                        AlertEntity(
                            id = 103L,
                            title = "Medicine reminder completed at 8:00 AM",
                            message = "Morning BP medication logged as completed.",
                            severity = "INFO",
                            timestamp = now - (3600000L * 7),
                            isRead = true
                        ),
                        AlertEntity(
                            id = 104L,
                            title = "Upcoming doctor appointment tomorrow at 3:00 PM",
                            message = "Routine checkup with Dr. Sharma scheduled at Apollo Clinic.",
                            severity = "INFO",
                            timestamp = now - (3600000L * 2),
                            isRead = false
                        )
                    )
                } else alerts

                val unreadCount = listToDisplay.count { !it.isRead }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    allAlerts = listToDisplay,
                    unreadCount = unreadCount
                )
                applyFilter()
            }
        }
    }

    fun setFilter(filter: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
        applyFilter()
    }

    fun markAsRead(alertId: Long) {
        viewModelScope.launch {
            alertRepository.markAsRead(alertId)
            val updated = _uiState.value.allAlerts.map {
                if (it.id == alertId) it.copy(isRead = true) else it
            }
            _uiState.value = _uiState.value.copy(
                allAlerts = updated,
                unreadCount = updated.count { !it.isRead }
            )
            applyFilter()
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            _uiState.value.allAlerts.forEach { alert ->
                if (!alert.isRead) {
                    alertRepository.markAsRead(alert.id)
                }
            }
            val updated = _uiState.value.allAlerts.map { it.copy(isRead = true) }
            _uiState.value = _uiState.value.copy(
                allAlerts = updated,
                unreadCount = 0
            )
            applyFilter()
        }
    }

    private fun applyFilter() {
        val current = _uiState.value
        val filtered = when (current.selectedFilter) {
            "UNREAD" -> current.allAlerts.filter { !it.isRead }
            "HIGH" -> current.allAlerts.filter { it.severity.equals("HIGH", ignoreCase = true) }
            else -> current.allAlerts
        }
        _uiState.value = current.copy(filteredAlerts = filtered)
    }
}
