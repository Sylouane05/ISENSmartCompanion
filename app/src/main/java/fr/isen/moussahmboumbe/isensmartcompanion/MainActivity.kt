package fr.isen.moussahmboumbe.isensmartcompanion

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.core.content.ContextCompat
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
import fr.isen.moussahmboumbe.isensmartcompanion.notifications.ReminderService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Demande la permission pour les notifications (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }

        enableEdgeToEdge()
        setContent {
            ISENSmartCompanionTheme {
                MainScreen()
            }
        }
    }

    // ✅ Fonction pour demander la permission de notification
    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (!isGranted) {
                    Toast.makeText(this, "Permission de notification refusée", Toast.LENGTH_SHORT).show()
                }
            }
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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

// ✅ Écran d'accueil avec Gemini AI
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

// ✅ Écran des événements avec notifications
@Composable
fun EventsScreen() {
    val context = LocalContext.current
    var events by remember { mutableStateOf<List<Event>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val reminderService = remember { ReminderService(context) }
    var subscribedEvents by remember { mutableStateOf(mutableSetOf<String>()) }

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
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = event.title, fontSize = 20.sp, color = Color.Black)
                            Text(text = event.date, fontSize = 14.sp, color = Color.Gray)
                            Text(text = event.location, fontSize = 14.sp, color = Color.DarkGray)

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(onClick = {
                                if (subscribedEvents.contains(event.id)) {
                                    subscribedEvents.remove(event.id)
                                    reminderService.cancelNotification(event.id, event.title)
                                    Toast.makeText(context, "Désabonné de ${event.title}", Toast.LENGTH_SHORT).show()
                                } else {
                                    subscribedEvents.add(event.id)
                                    reminderService.scheduleNotification(event.id, event.title, event.description)
                                    Toast.makeText(context, "Abonné à ${event.title}", Toast.LENGTH_SHORT).show()
                                }
                            }) {
                                Text(text = if (subscribedEvents.contains(event.id)) "Se désabonner" else "S'abonner")
                            }
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