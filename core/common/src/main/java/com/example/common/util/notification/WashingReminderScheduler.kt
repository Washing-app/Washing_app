package com.example.common.util.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.time.ZoneId

class WashingReminderScheduler(
    private val context: Context
) {

    fun schedule(
        bookingId: String,
        startTime: String,
        endTime: String
    ) {
        scheduleNotification(
            requestCode = "${bookingId}_start".hashCode(),
            triggerAtMillis = parseMillis(startTime) - FIFTEEN_MINUTES,
            message = "У вас предстоящая стирка через 15 минут"
        )

        scheduleNotification(
            requestCode = "${bookingId}_end".hashCode(),
            triggerAtMillis = parseMillis(endTime),
            message = "Стирка окончена! Пожалуйста заберите ваши вещи."
        )
    }

    private fun scheduleNotification(
        requestCode: Int,
        triggerAtMillis: Long,
        message: String
    ) {
        if (triggerAtMillis <= System.currentTimeMillis()) return

        val intent = Intent(context, WashingReminderReceiver::class.java).apply {
            putExtra(WashingReminderReceiver.EXTRA_MESSAGE, message)
            putExtra(WashingReminderReceiver.EXTRA_NOTIFICATION_ID, requestCode)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(AlarmManager::class.java)
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    private fun parseMillis(value: String): Long {
        return LocalDateTime
            .parse(value)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    companion object {
        private const val FIFTEEN_MINUTES = 15 * 60 * 1000L
    }
}