package com.example.myapplication.utils

import android.R
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.myapplication.data.local.entity.ReminderEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object ReminderNotificationHelper {

    const val CHANNEL_ID = "care_reminders_channel"
    const val CHANNEL_NAME = "Care Reminders"
    const val ACTION_TRIGGER_REMINDER = "com.example.myapplication.ACTION_REMINDER_TRIGGER"

    const val EXTRA_REMINDER_ID = "reminder_id"
    const val EXTRA_REMINDER_TITLE = "reminder_title"
    const val EXTRA_REMINDER_CATEGORY = "reminder_category"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "High-priority care notifications for medication, hydration, and appointments"
                enableVibration(true)
                enableLights(true)
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleNotification(context: Context, reminder: ReminderEntity) {
        createNotificationChannel(context)
        if (!reminder.isEnabled) {
            cancelNotification(context, reminder.id)
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = ACTION_TRIGGER_REMINDER
            putExtra(EXTRA_REMINDER_ID, reminder.id)
            putExtra(EXTRA_REMINDER_TITLE, reminder.title)
            putExtra(EXTRA_REMINDER_CATEGORY, reminder.category)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = parseTimeStringToMillis(reminder.timeString)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelNotification(context: Context, reminderId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = ACTION_TRIGGER_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    fun showImmediateNotification(
        context: Context,
        title: String,
        message: String,
        category: String,
        notificationId: Int = System.currentTimeMillis().toInt()
    ) {
        createNotificationChannel(context)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_popup_reminder)
            .setContentTitle("Arogya Care Reminder: $category")
            .setContentText(if (message.isNotBlank()) "$title - $message" else title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }

    private fun parseTimeStringToMillis(timeString: String): Long {
        val calendar = Calendar.getInstance()
        val now = System.currentTimeMillis()

        try {
            val sdf12 = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val sdf24 = SimpleDateFormat("HH:mm", Locale.getDefault())

            val parsedDate = try {
                sdf12.parse(timeString)
            } catch (e: Exception) {
                try {
                    sdf24.parse(timeString)
                } catch (e2: Exception) {
                    null
                }
            }

            if (parsedDate != null) {
                val parsedCalendar = Calendar.getInstance().apply { time = parsedDate }
                calendar.set(Calendar.HOUR_OF_DAY, parsedCalendar.get(Calendar.HOUR_OF_DAY))
                calendar.set(Calendar.MINUTE, parsedCalendar.get(Calendar.MINUTE))
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                if (calendar.timeInMillis <= now) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
                return calendar.timeInMillis
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Fallback: 1 hour from now
        return now + (60 * 60 * 1000)
    }
}
