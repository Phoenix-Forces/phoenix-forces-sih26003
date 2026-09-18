package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.ReminderDao
import com.example.myapplication.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

class ReminderRepository(private val reminderDao: ReminderDao) {
    val allReminders: Flow<List<ReminderEntity>> = reminderDao.getAllReminders()
    val activeReminders: Flow<List<ReminderEntity>> = reminderDao.getActiveReminders()

    fun getReminderById(id: Long): Flow<ReminderEntity?> {
        return reminderDao.getReminderById(id)
    }

    suspend fun addReminder(reminder: ReminderEntity): Long {
        return reminderDao.insertReminder(reminder)
    }

    suspend fun updateReminder(reminder: ReminderEntity) {
        reminderDao.updateReminder(reminder)
    }

    suspend fun toggleReminderStatus(id: Long, isEnabled: Boolean) {
        reminderDao.setReminderEnabled(id, isEnabled)
    }

    suspend fun deleteReminder(reminder: ReminderEntity) {
        reminderDao.deleteReminder(reminder)
    }
}
