package com.example.myapplication.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("game-results")
    suspend fun createGameResult(
        @Body request: GameResultRequest
    ): GameResultResponse
}