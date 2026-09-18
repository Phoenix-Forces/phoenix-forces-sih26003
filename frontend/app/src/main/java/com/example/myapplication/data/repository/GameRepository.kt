package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.GameResultDao
import com.example.myapplication.data.local.entity.GameResultEntity
import com.example.myapplication.data.remote.GameResultRequest
import com.example.myapplication.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow

class GameRepository(
    private val gameResultDao: GameResultDao
) {

    val allResults: Flow<List<GameResultEntity>> =
        gameResultDao.getAllResults()

    fun getResultsByDomain(
        domain: String
    ): Flow<List<GameResultEntity>> {
        return gameResultDao.getResultsByDomain(domain)
    }

    fun getRecentResults(
        limit: Int = 10
    ): Flow<List<GameResultEntity>> {
        return gameResultDao.getRecentResults(limit)
    }

    suspend fun saveGameResult(
        result: GameResultEntity,
        patientId: String = "1"
    ): Long {

        // 1. Always save locally first.
        // This keeps the app working offline.
        val localId = gameResultDao.insertResult(result)

        // 2. Try to synchronize the same result with AWS.
        // A network failure must NOT break the game.
        try {
            val request = GameResultRequest(
                patientId = patientId,
                gameType = result.gameType,
                score = result.score,
                correctCount = result.correctCount,
                incorrectCount = result.incorrectCount,
                timeTakenSeconds = result.timeTakenSeconds,
                difficultyLevel = result.difficultyLevel,
                timestamp = result.timestamp,
                cognitiveDomain = result.cognitiveDomain
            )

            RetrofitClient.apiService.createGameResult(request)

        } catch (e: Exception) {
            // Offline or API failure:
            // local Room data remains safely stored.
        }

        return localId
    }
}