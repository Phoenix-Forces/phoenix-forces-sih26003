package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 1L,
    val name: String,
    val age: Int,
    val caregiverName: String,
    val primaryLanguage: String,
    val currentDifficulty: String,
    val streakDays: Int,
    val lastActiveDate: String
)
