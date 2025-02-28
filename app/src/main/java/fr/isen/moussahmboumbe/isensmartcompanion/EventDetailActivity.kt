package fr.isen.moussahmboumbe.isensmartcompanion

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.moussahmboumbe.isensmartcompanion.notifications.EventReminderManager
import fr.isen.moussahmboumbe.isensmartcompanion.notifications.ReminderService
import kotlinx.coroutines.launch

class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val eventId = intent.getStringExtra("event_id") ?: return
        val eventTitle = intent.getStringExtra("event_title") ?: "Événement"
        val eventDescription = intent.getStringExtra("event_description") ?: "Aucune description"
        val eventLocation = intent.getStringExtra("event_location") ?: "Lieu inconnu"
        val eventDate = intent.getStringExtra("event_date") ?: "Date non précisée"

        setContent {
            EventDetailScreen(eventId, eventTitle, eventDescription, eventDate, eventLocation)
        }
    }
}

@Composable
fun EventDetailScreen(eventId: String, eventTitle: String, eventDescription: String, eventDate: String, eventLocation: String) {
    val context = LocalContext.current
    val reminderManager = remember { EventReminderManager(context) }
    val reminderService = remember { ReminderService(context) }
    val coroutineScope = rememberCoroutineScope()
    var isReminderSet by remember { mutableStateOf(false) }

    LaunchedEffect(eventId) {
        reminderManager.isEventReminderSet(eventId).collect { isReminderSet = it }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = eventTitle, style = MaterialTheme.typography.headlineSmall)
        Text(text = "📍 $eventLocation", style = MaterialTheme.typography.bodyMedium)
        Text(text = "📅 $eventDate", style = MaterialTheme.typography.bodyMedium)
        Text(text = eventDescription, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    reminderManager.toggleEventReminder(eventId)
                    isReminderSet = !isReminderSet
                }
                if (isReminderSet) {
                    Toast.makeText(context, "Rappel activé", Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        reminderService.sendNotification()
                    }, 10000) // ✅ Envoie la notification après 10 secondes
                } else {
                    Toast.makeText(context, "Rappel désactivé", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(if (isReminderSet) "🔔 Désactiver le rappel" else "🔔 Activer le rappel")
        }
    }
}
