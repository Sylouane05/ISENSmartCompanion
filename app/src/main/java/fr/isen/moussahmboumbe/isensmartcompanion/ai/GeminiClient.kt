package fr.isen.moussahmboumbe.isensmartcompanion.ai

import android.content.Context
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import fr.isen.moussahmboumbe.isensmartcompanion.R

object GeminiClient {

    private lateinit var apiKey: String
    private lateinit var generativeModel: GenerativeModel

    /**
     *  Initialiser Gemini avec la clé API depuis `strings.xml`
     */
    fun initialize(context: Context) {
        apiKey = context.getString(R.string.GEMINI_API_KEY) //  Récupère la clé API depuis `strings.xml`
        Log.d("GeminiClient", "Clé API chargée : $apiKey") //  Vérifie si la clé API est bien chargée
        generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey
        )
    }

    /**
     *  Récupérer une réponse de l'IA Gemini 1.5
     */
    suspend fun getResponse(question: String): String {
        return try {
            Log.d("GeminiClient", "Envoi de la question : $question") // Log pour voir la question envoyée
            val response: GenerateContentResponse = generativeModel.generateContent(question)
            response.text ?: "Je n'ai pas compris la question."
        } catch (e: Exception) {
            Log.e("GeminiClient", "Erreur API: ${e.message}") //  Affiche l'erreur exacte
            "Une erreur est survenue, réessaie plus tard."
        }
    }
}
