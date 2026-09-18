package com.example.myapplication.data.remote

data class GameResultResponse(
    val status: String,
    val message: String,
    val resultId: String,
    val accuracy: Double
)