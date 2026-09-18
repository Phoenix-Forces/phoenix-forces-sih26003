package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val category: String,
    val timeString: String,
    val isEnabled: Boolean = true,
    val repeatInterval: String = "Daily",
    val notes: String = ""
)
