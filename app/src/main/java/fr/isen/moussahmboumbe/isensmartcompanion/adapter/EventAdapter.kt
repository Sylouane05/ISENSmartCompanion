package fr.isen.moussahmboumbe.isensmartcompanion.adapter

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import fr.isen.moussahmboumbe.isensmartcompanion.R
import fr.isen.moussahmboumbe.isensmartcompanion.model.Event

class EventAdapter(
    private val events: List<Event>,
    param: (Any) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private val handler = Handler(Looper.getMainLooper())

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val category: TextView = view.findViewById(R.id.categorieEvent)
        val title: TextView = view.findViewById(R.id.titreEvent)
        val location: TextView = view.findViewById(R.id.lieuEvent)
        val date: TextView = view.findViewById(R.id.dateEvent)
        val notificationIcon: ImageView = view.findViewById(R.id.iconNotification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.category.text = event.category
        holder.title.text = event.title
        holder.location.text = " ${event.location}"
        holder.date.text = " ${event.date}"

        updateNotificationIcon(holder.notificationIcon, event.notification)

        holder.notificationIcon.setOnClickListener {
            event.notification = !event.notification
            updateNotificationIcon(holder.notificationIcon, event.notification)
            updateEventInDatabase(event)

            if (event.notification) {
                handler.postDelayed({
                    sendNotification(event, holder.itemView.context)
                }, 10_000) // 10 secondes
            }
        }

        holder.itemView.setOnClickListener {
            showPopup(holder.itemView, event)
        }
    }

    override fun getItemCount(): Int = events.size

    private fun updateNotificationIcon(icon: ImageView, isActive: Boolean) {
        if (isActive) {
            icon.setImageResource(R.drawable.ic_notifon)
        } else {
            icon.setImageResource(R.drawable.ic_notifoff)
        }
    }

    private fun sendNotification(event: Event, context: Context) {
        val channelId = "event_notifications"
        val notificationId = event.id.hashCode()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Event Notifications"
            val descriptionText = "Notifications pour les événements"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notifon)
            .setContentTitle("Inscription confirmée ")
            .setContentText("Tu es inscrit à l'événement : ${event.title}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(notificationId, notification.build())
        }
    }

    private fun showPopup(view: View, event: Event) {
        val popupView = LayoutInflater.from(view.context).inflate(R.layout.popup_event, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupView.findViewById<TextView>(R.id.categoryPop).text = event.category
        popupView.findViewById<TextView>(R.id.titlePop).text = event.title
        popupView.findViewById<TextView>(R.id.datePop).text = " ${event.date}"
        popupView.findViewById<TextView>(R.id.locationPop).text = " ${event.location}"
        popupView.findViewById<TextView>(R.id.descriptionPop).text = event.description

        val notifIcon = popupView.findViewById<ImageView>(R.id.notifPop)
        updateNotificationIcon(notifIcon, event.notification)

        notifIcon.setOnClickListener {
            event.notification = !event.notification
            updateNotificationIcon(notifIcon, event.notification)
            updateEventInDatabase(event)

            if (event.notification) {
                handler.postDelayed({
                    sendNotification(event, view.context)
                }, 10_000)
            }
        }

        popupView.findViewById<ImageView>(R.id.closePop).setOnClickListener {
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    private fun updateEventInDatabase(event: Event) {
        val database = FirebaseDatabase.getInstance("https://isensmartsompanion-default-rtdb.firebaseio.com/")
            .getReference("events")
            .child(event.id)

        database.child("notification").setValue(event.notification)
    }
}
