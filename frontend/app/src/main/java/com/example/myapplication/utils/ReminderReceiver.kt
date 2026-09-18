package com.example.myapplication.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(ReminderNotificationHelper.EXTRA_REMINDER_TITLE) ?: "Care Reminder"
        val category = intent.getStringExtra(ReminderNotificationHelper.EXTRA_REMINDER_CATEGORY) ?: "Daily Activity"
        val reminderId = intent.getLongExtra(ReminderNotificationHelper.EXTRA_REMINDER_ID, System.currentTimeMillis()).toInt()

        ReminderNotificationHelper.showImmediateNotification(
            context = context,
            title = title,
            message = "Time for your scheduled care activity",
            category = category,
            notificationId = reminderId
        )
    }
}
