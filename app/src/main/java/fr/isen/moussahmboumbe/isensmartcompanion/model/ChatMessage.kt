package fr.isen.moussahmboumbe.isensmartcompanion.model

data class ChatMessage(
    val message: String,
    val isUser: Boolean // true = message de l'utilisateur, false = réponse IA
)
