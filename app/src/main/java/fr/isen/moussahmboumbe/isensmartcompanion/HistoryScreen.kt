package fr.isen.moussahmboumbe.isensmartcompanion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.isen.moussahmboumbe.isensmartcompanion.database.AppDatabase
import fr.isen.moussahmboumbe.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen() {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val chatDao = database.chatDao()
    val chatMessages by chatDao.getAllMessages().collectAsStateWithLifecycle(initialValue = emptyList())
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Historique des interactions",
            fontSize = MaterialTheme.typography.headlineSmall.fontSize
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (chatMessages.isEmpty()) {
            Text(
                text = "Aucun historique disponible",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.secondary
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // ✅ Permet de laisser de la place au bouton en bas
                    .fillMaxWidth()
            ) {
                items(chatMessages) { message ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Q: ${message.question}",
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize
                            )
                            Text(
                                text = "A: ${message.response}",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // ✅ Bouton pour supprimer un seul message
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        chatDao.deleteMessage(message.id)
                                    }
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("🗑️ Supprimer")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ✅ Bouton pour supprimer tout l'historique (garanti visible)
            Button(
                onClick = {
                    coroutineScope.launch {
                        chatDao.deleteAllMessages()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(text = "🗑️ Effacer tout", color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    ISENSmartCompanionTheme {
        HistoryScreen()
    }
}
