package fr.isen.moussahmboumbe.isensmartcompanion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import fr.isen.moussahmboumbe.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import fr.isen.moussahmboumbe.isensmartcompanion.model.Event
import fr.isen.moussahmboumbe.isensmartcompanion.model.ChatMessage
import fr.isen.moussahmboumbe.isensmartcompanion.api.RetrofitInstance
import fr.isen.moussahmboumbe.isensmartcompanion.ai.GeminiAIService
import fr.isen.moussahmboumbe.isensmartcompanion.database.AppDatabase
import fr.isen.moussahmboumbe.isensmartcompanion.database.ChatDao
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ISENSmartCompanionTheme {
                MainScreen()
            }
        }
    }
}

// ✅ Fonction principale qui gère la navigation
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "accueil",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("accueil") { HomeScreen() }
            composable("evenements") { EventsScreen() }
            composable("historique") { HistoryScreen() }
        }
    }
}

// ✅ Barre de navigation
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf("accueil", "evenements", "historique")

    NavigationBar {
        val currentRoute = navController.currentDestination?.route
        items.forEach { screen ->
            NavigationBarItem(
                icon = {},
                label = { Text(screen.replaceFirstChar { it.uppercase() }) },
                selected = currentRoute == screen,
                onClick = {
                    navController.navigate(screen) {
                        popUpTo("accueil") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

// ✅ Écran d'accueil avec Gemini AI (Correction: Utilisation du callback)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    var question by remember { mutableStateOf("") }
    var responses by remember { mutableStateOf(listOf<ChatMessage>()) }
    val aiService = remember { GeminiAIService(context) }
    val coroutineScope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context) }
    val chatDao = database.chatDao()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.isen),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )
        Text(text = "ISEN Smart Companion", fontSize = 24.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Posez votre question") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            if (question.isNotBlank()) {
                Toast.makeText(context, "Question soumise", Toast.LENGTH_SHORT).show()

                coroutineScope.launch {
                    aiService.getAIResponse(question) { aiResponse ->
                        val chatMessage = ChatMessage(question = question, response = aiResponse)
                        responses = responses + chatMessage
                        question = ""

                        // ✅ Stocke dans Room
                        coroutineScope.launch {
                            chatDao.insertMessage(chatMessage)
                        }
                    }
                }
            }
        }) {
            Text(text = "Envoyer")
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(responses) { response ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Q: ${response.question}", fontSize = 16.sp, color = Color.Black)
                        Text(text = "A: ${response.response}", fontSize = 16.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}





// ✅ Écran des événements récupérés via l'API
@Composable
fun EventsScreen() {
    val context = LocalContext.current
    var events by remember { mutableStateOf<List<Event>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        RetrofitInstance.api.getEvents().enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                if (response.isSuccessful) {
                    events = response.body()
                } else {
                    errorMessage = "Erreur lors de la récupération des événements"
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                errorMessage = "Impossible de récupérer les événements : ${t.message}"
            }
        })
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Événements ISEN", fontSize = 24.sp, color = Color.Black)

        if (errorMessage != null) {
            Text(text = errorMessage!!, color = Color.Red, modifier = Modifier.padding(8.dp))
        } else if (events.isNullOrEmpty()) {
            Text(text = "Aucun événement disponible", fontSize = 16.sp, color = Color.Gray)
        } else {
            LazyColumn {
                items(events!!) { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                val intent = Intent(context, EventDetailActivity::class.java).apply {
                                    putExtra("event_id", event.id)
                                    putExtra("event_title", event.title)
                                    putExtra("event_description", event.description)
                                    putExtra("event_date", event.date)
                                    putExtra("event_location", event.location)
                                    putExtra("event_category", event.category)
                                }
                                context.startActivity(intent)
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = event.title, fontSize = 20.sp, color = Color.Black)
                            Text(text = event.date, fontSize = 14.sp, color = Color.Gray)
                            Text(text = event.location, fontSize = 14.sp, color = Color.DarkGray)
                        }
                    }
                }
            }
        }
    }
}



// ✅ Preview pour tester l'affichage dans l'éditeur
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ISENSmartCompanionTheme {
        MainScreen()
    }
}
