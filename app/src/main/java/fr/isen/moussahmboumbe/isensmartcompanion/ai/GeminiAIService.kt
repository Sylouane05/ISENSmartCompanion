package fr.isen.moussahmboumbe.isensmartcompanion.ai

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.moussahmboumbe.isensmartcompanion.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeminiAIService(context: Context) {

    //  Charge la clé API depuis `gradle.properties`
    private val apiKey: String = BuildConfig.GEMINI_API_KEY

    //  Vérifie si la clé API est bien chargée
    init {
        println(" Clé API chargée : $apiKey") //  Vérifier dans Logcat
    }

    //  Création du modèle Gemini AI
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey //  Utilisation de la clé API correcte
    )

    //  Fonction pour récupérer la réponse de Gemini AI via un callback
    fun getAIResponse(userInput: String, callback: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = generativeModel.generateContent(userInput)
                val result = response.text ?: "Je n'ai pas compris votre question."
                callback(result)
            } catch (e: Exception) {
                callback("Erreur Gemini AI : ${e.message}")
            }
        }
    }
}
