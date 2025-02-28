package fr.isen.moussahmboumbe.isensmartcompanion.notifications

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import fr.isen.moussahmboumbe.isensmartcompanion.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val eventId = intent.getStringExtra("event_id") ?: "0"
            val eventTitle = intent.getStringExtra("event_title") ?: "Événement"
            val eventDescription = intent.getStringExtra("event_description") ?: "Détails indisponibles"

            val notification = NotificationCompat.Builder(context, "event_notifications")
                .setSmallIcon(R.drawable.notif) // ✅ Assurez-vous que ce fichier existe dans "res/drawable"
                .setContentTitle("Rappel : $eventTitle")
                .setContentText(eventDescription)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context, //
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return // Quitte la fonction si la permission n'est pas accordée
                }
                notify(eventId.hashCode(), notification)
            }
        }
    }
}
