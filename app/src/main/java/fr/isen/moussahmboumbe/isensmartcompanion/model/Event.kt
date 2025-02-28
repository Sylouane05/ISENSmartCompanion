package fr.isen.moussahmboumbe.isensmartcompanion.model

data class Event(
    val id: String,  //  Correction : l'ID est maintenant une String
    val title: String,
    val description: String,
    val date: String,
    val location: String,  //  Correction : Correspondance avec le JSON
    val category: String
)
