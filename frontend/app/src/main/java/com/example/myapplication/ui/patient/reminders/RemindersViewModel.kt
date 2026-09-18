package com.example.myapplication.ui.patient.reminders

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.ReminderEntity
import com.example.myapplication.data.repository.ReminderRepository
import com.example.myapplication.utils.ReminderNotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class RemindersUiState(
    val reminders: List<ReminderEntity> = emptyList(),
    val selectedCategoryFilter: String = "All",
    val isLoading: Boolean = false
)

class RemindersViewModel(
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("All")

    val uiState: StateFlow<RemindersUiState> = combine(
        reminderRepository.allReminders,
        _selectedCategory
    ) { allReminders, category ->
        val filtered = if (category == "All") {
            allReminders
        } else {
            allReminders.filter { it.category.equals(category, ignoreCase = true) }
        }
        RemindersUiState(
            reminders = filtered,
            selectedCategoryFilter = category
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RemindersUiState()
    )

    fun filterByCategory(category: String) {
        _selectedCategory.value = category
    }

    fun addReminder(
        title: String,
        category: String,
        timeString: String,
        repeatInterval: String,
        notes: String,
        context: Context
    ) {
        viewModelScope.launch {
            val reminder = ReminderEntity(
                title = title,
                category = category,
                timeString = timeString,
                isEnabled = true,
                repeatInterval = repeatInterval,
                notes = notes
            )
            val newId = reminderRepository.addReminder(reminder)
            val fullReminder = reminder.copy(id = newId)
            ReminderNotificationHelper.scheduleNotification(context, fullReminder)
        }
    }

    fun updateReminder(reminder: ReminderEntity, context: Context) {
        viewModelScope.launch {
            reminderRepository.updateReminder(reminder)
            ReminderNotificationHelper.scheduleNotification(context, reminder)
        }
    }

    fun toggleReminderStatus(reminder: ReminderEntity, isEnabled: Boolean, context: Context) {
        viewModelScope.launch {
            reminderRepository.toggleReminderStatus(reminder.id, isEnabled)
            val updated = reminder.copy(isEnabled = isEnabled)
            ReminderNotificationHelper.scheduleNotification(context, updated)
        }
    }

    fun deleteReminder(reminder: ReminderEntity, context: Context) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(reminder)
            ReminderNotificationHelper.cancelNotification(context, reminder.id)
        }
    }

    fun triggerTestNotification(context: Context) {
        ReminderNotificationHelper.showImmediateNotification(
            context = context,
            title = "Medicine Care Alert",
            message = "Time for your scheduled Assam Herbal Tea & Morning Tablets!",
            category = "Medicine"
        )
    }
}
