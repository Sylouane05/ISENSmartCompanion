package fr.isen.moussahmboumbe.isensmartcompanion.api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.moussahmboumbe.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val eventId = intent.getStringExtra("event_id") ?: "ID inconnu" // ✅ Correction
        val eventTitle = intent.getStringExtra("event_title") ?: "Événement inconnu"
        val eventDescription = intent.getStringExtra("event_description") ?: "Pas de description"
        val eventDate = intent.getStringExtra("event_date") ?: "Date inconnue"
        val eventLocation = intent.getStringExtra("event_location") ?: "Lieu inconnu"
        val eventCategory = intent.getStringExtra("event_category") ?: "Catégorie inconnue"

        setContent {
            ISENSmartCompanionTheme {
                EventDetailScreen(
                    id = eventId,
                    title = eventTitle,
                    description = eventDescription,
                    date = eventDate,
                    location = eventLocation,
                    category = eventCategory
                )
            }
        }
    }
}

@Composable
fun EventDetailScreen(id: String, title: String, description: String, date: String, location: String, category: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title, fontSize = 24.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Date: $date", fontSize = 16.sp, color = Color.Gray)
        Text(text = "Lieu: $location", fontSize = 16.sp, color = Color.DarkGray)
        Text(text = "Catégorie: $category", fontSize = 16.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = description, fontSize = 18.sp, color = Color.Black)
    }
}
