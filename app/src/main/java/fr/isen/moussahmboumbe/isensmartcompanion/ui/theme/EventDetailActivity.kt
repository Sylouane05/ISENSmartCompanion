package fr.isen.moussahmboumbe.isensmartcompanion.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.TextView
import fr.isen.moussahmboumbe.isensmartcompanion.R

class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event)

        val eventTitle = findViewById<TextView>(R.id.titreEvent)
        val eventLocation = findViewById<TextView>(R.id.lieuEvent)
        val eventDateTime = findViewById<TextView>(R.id.dateEvent)

        val title = intent.getStringExtra("event_name") ?: "Événement inconnu"
        val location = intent.getStringExtra("event_location") ?: "Lieu inconnu"
        val dateTime = intent.getStringExtra("event_date") ?: "Date inconnue"
        val category = intent.getStringExtra("event_category") ?: "Catégorie inconnue"

        eventTitle.text = title
        eventLocation.text = " $location"
        eventDateTime.text = " $dateTime"



    }
}