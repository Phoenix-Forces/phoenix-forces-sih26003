package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.local.entity.GameResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameResultDao {
    @Query("SELECT * FROM game_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<GameResultEntity>>

    @Query("SELECT * FROM game_results WHERE cognitiveDomain = :domain ORDER BY timestamp DESC")
    fun getResultsByDomain(domain: String): Flow<List<GameResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: GameResultEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(results: List<GameResultEntity>)

    @Query("SELECT * FROM game_results ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentResults(limit: Int = 10): Flow<List<GameResultEntity>>
}
