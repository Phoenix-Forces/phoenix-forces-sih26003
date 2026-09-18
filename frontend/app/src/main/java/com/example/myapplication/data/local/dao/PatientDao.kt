package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.local.entity.PatientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Query("SELECT * FROM patients WHERE id = 1 LIMIT 1")
    fun getPatient(): Flow<PatientEntity?>

    @Query("SELECT * FROM patients WHERE id = 1 LIMIT 1")
    suspend fun getPatientDirect(): PatientEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: PatientEntity)

    @Update
    suspend fun updatePatient(patient: PatientEntity)

    @Query("UPDATE patients SET streakDays = streakDays + 1, lastActiveDate = :today WHERE id = 1")
    suspend fun incrementStreak(today: String)

    @Query("UPDATE patients SET primaryLanguage = :language WHERE id = 1")
    suspend fun updateLanguage(language: String)

    @Query("UPDATE patients SET currentDifficulty = :difficulty WHERE id = 1")
    suspend fun updateDifficulty(difficulty: String)
}
