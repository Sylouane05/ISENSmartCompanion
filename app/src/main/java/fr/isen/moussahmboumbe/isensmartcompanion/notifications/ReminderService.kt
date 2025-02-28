package fr.isen.moussahmboumbe.isensmartcompanion.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import fr.isen.moussahmboumbe.isensmartcompanion.R

class ReminderService(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "event_notifications"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Rappels d'événements",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleNotification(eventId: String, eventTitle: String, eventDescription: String) {
        sendInstantNotification(eventId, "Vous vous êtes abonné à $eventTitle")
    }

    fun cancelNotification(eventId: String, eventTitle: String) {
        sendInstantNotification(eventId, "Vous vous êtes désabonné de $eventTitle")
    }

    private fun sendInstantNotification(eventId: String, message: String) {
        val notificationManager = NotificationManagerCompat.from(context)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notif)
            .setContentTitle("ISEN Smart Companion")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(eventId.hashCode(), notification)
    }
}
