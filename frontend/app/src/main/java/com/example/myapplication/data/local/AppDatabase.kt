package com.example.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.local.dao.AlertDao
import com.example.myapplication.data.local.dao.GameResultDao
import com.example.myapplication.data.local.dao.PatientDao
import com.example.myapplication.data.local.dao.ReminderDao
import com.example.myapplication.data.local.entity.AlertEntity
import com.example.myapplication.data.local.entity.GameResultEntity
import com.example.myapplication.data.local.entity.PatientEntity
import com.example.myapplication.data.local.entity.ReminderEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Database(
    entities = [
        PatientEntity::class,
        GameResultEntity::class,
        ReminderEntity::class,
        AlertEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun patientDao(): PatientDao
    abstract fun gameResultDao(): GameResultDao
    abstract fun reminderDao(): ReminderDao
    abstract fun alertDao(): AlertDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mdoner_app.db"
                )
                    .addCallback(AppDatabaseCallback(context.applicationContext))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateSeedData(database)
                    }
                }
            }
        }

        private suspend fun populateSeedData(db: AppDatabase) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val todayStr = dateFormat.format(Date())

            // 1. Seed Patient Data
            db.patientDao().insertPatient(
                PatientEntity(
                    id = 1L,
                    name = "Ramesh Kumar",
                    age = 72,
                    caregiverName = "Anita Kumar",
                    primaryLanguage = "en",
                    currentDifficulty = "MEDIUM",
                    streakDays = 5,
                    lastActiveDate = todayStr
                )
            )

            // 2. Seed Game Results for all 6 cognitive domains
            val currentTime = System.currentTimeMillis()
            val dayMillis = 86400000L

            val seedResults = listOf(
                GameResultEntity(
                    gameType = "Memory Match",
                    score = 85,
                    correctCount = 8,
                    incorrectCount = 2,
                    timeTakenSeconds = 45,
                    difficultyLevel = "MEDIUM",
                    timestamp = currentTime - (dayMillis * 1),
                    cognitiveDomain = "Memory Match"
                ),
                GameResultEntity(
                    gameType = "Pattern Memory",
                    score = 90,
                    correctCount = 9,
                    incorrectCount = 1,
                    timeTakenSeconds = 30,
                    difficultyLevel = "MEDIUM",
                    timestamp = currentTime - (dayMillis * 2),
                    cognitiveDomain = "Pattern Memory"
                ),
                GameResultEntity(
                    gameType = "Focus Test",
                    score = 75,
                    correctCount = 7,
                    incorrectCount = 3,
                    timeTakenSeconds = 60,
                    difficultyLevel = "EASY",
                    timestamp = currentTime - (dayMillis * 3),
                    cognitiveDomain = "Attention"
                ),
                GameResultEntity(
                    gameType = "Daily Schedule",
                    score = 95,
                    correctCount = 10,
                    incorrectCount = 0,
                    timeTakenSeconds = 40,
                    difficultyLevel = "MEDIUM",
                    timestamp = currentTime - (dayMillis * 4),
                    cognitiveDomain = "Daily Routine Recall"
                ),
                GameResultEntity(
                    gameType = "Identify Item",
                    score = 80,
                    correctCount = 8,
                    incorrectCount = 2,
                    timeTakenSeconds = 50,
                    difficultyLevel = "MEDIUM",
                    timestamp = currentTime - (dayMillis * 5),
                    cognitiveDomain = "Object Recognition"
                ),
                GameResultEntity(
                    gameType = "Identify Feeling",
                    score = 88,
                    correctCount = 9,
                    incorrectCount = 1,
                    timeTakenSeconds = 35,
                    difficultyLevel = "EASY",
                    timestamp = currentTime - (dayMillis * 6),
                    cognitiveDomain = "Emotion Recognition"
                )
            )
            db.gameResultDao().insertAll(seedResults)

            // 3. Seed Reminders (Medicine 8:00 AM, Hydration 11:00 AM, Doctor Appointment 3:00 PM)
            val seedReminders = listOf(
                ReminderEntity(
                    title = "Morning Medicine",
                    category = "Medicine",
                    timeString = "8:00 AM",
                    isEnabled = true,
                    repeatInterval = "Daily",
                    notes = "Take BP tablet after breakfast"
                ),
                ReminderEntity(
                    title = "Hydration Break",
                    category = "Hydration",
                    timeString = "11:00 AM",
                    isEnabled = true,
                    repeatInterval = "Daily",
                    notes = "Drink 1 glass of warm water"
                ),
                ReminderEntity(
                    title = "Doctor Appointment",
                    category = "Appointment",
                    timeString = "3:00 PM",
                    isEnabled = true,
                    repeatInterval = "Once",
                    notes = "Dr. Sharma at Apollo Clinic"
                )
            )
            db.reminderDao().insertAll(seedReminders)

            // 4. Seed Caregiver Alerts
            val seedAlerts = listOf(
                AlertEntity(
                    title = "Missed Evening Medication",
                    message = "Ramesh missed taking evening BP medication yesterday.",
                    severity = "HIGH",
                    timestamp = currentTime - (3600000L * 5),
                    isRead = false
                ),
                AlertEntity(
                    title = "Cognitive Score Improved",
                    message = "Pattern Memory score increased by 15% this week!",
                    severity = "INFO",
                    timestamp = currentTime - (3600000L * 2),
                    isRead = false
                ),
                AlertEntity(
                    title = "Low Daily Hydration",
                    message = "Patient logged less water intake today.",
                    severity = "MEDIUM",
                    timestamp = currentTime - (3600000L * 10),
                    isRead = true
                )
            )
            db.alertDao().insertAll(seedAlerts)
        }
    }
}
