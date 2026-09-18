package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.AlertDao
import com.example.myapplication.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

class AlertRepository(private val alertDao: AlertDao) {
    val allAlerts: Flow<List<AlertEntity>> = alertDao.getAllAlerts()
    val unreadAlerts: Flow<List<AlertEntity>> = alertDao.getUnreadAlerts()

    suspend fun addAlert(alert: AlertEntity): Long {
        return alertDao.insertAlert(alert)
    }

    suspend fun markAsRead(alertId: Long) {
        alertDao.markAsRead(alertId)
    }
}
