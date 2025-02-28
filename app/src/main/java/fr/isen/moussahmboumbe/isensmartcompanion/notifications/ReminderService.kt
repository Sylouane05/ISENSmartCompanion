package fr.isen.moussahmboumbe.isensmartcompanion.notifications

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build

class ReminderService(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "event_notifications"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Événements ISEN"
            val descriptionText = "Notifications des événements auxquels vous êtes abonnés"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(eventTitle: String, eventDescription: String, delayMillis: Long = 10000) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("event_title", eventTitle)
            putExtra("event_description", eventDescription)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerTime = System.currentTimeMillis() + delayMillis

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }

    fun sendNotification() {

    }
}

class NotificationReceiver {

}
