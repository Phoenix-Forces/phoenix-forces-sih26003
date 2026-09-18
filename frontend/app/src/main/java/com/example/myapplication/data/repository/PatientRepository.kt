package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.PatientDao
import com.example.myapplication.data.local.entity.PatientEntity
import kotlinx.coroutines.flow.Flow

class PatientRepository(private val patientDao: PatientDao) {
    val patientFlow: Flow<PatientEntity?> = patientDao.getPatient()

    suspend fun getPatientDirect(): PatientEntity? {
        return patientDao.getPatientDirect()
    }

    suspend fun insertOrUpdatePatient(patient: PatientEntity) {
        patientDao.insertPatient(patient)
    }

    suspend fun incrementStreak(todayDateString: String) {
        patientDao.incrementStreak(todayDateString)
    }

    suspend fun updateLanguage(language: String) {
        patientDao.updateLanguage(language)
    }

    suspend fun updateDifficulty(difficulty: String) {
        patientDao.updateDifficulty(difficulty)
    }
}
