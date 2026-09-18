package com.example.myapplication.data.remote

data class GameResultRequest(
    val patientId: String,
    val gameType: String,
    val score: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val timeTakenSeconds: Int,
    val difficultyLevel: String,
    val timestamp: Long,
    val cognitiveDomain: String
)