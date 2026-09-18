package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_results")
data class GameResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val gameType: String,
    val score: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val timeTakenSeconds: Int,
    val difficultyLevel: String,
    val timestamp: Long,
    val cognitiveDomain: String
)
