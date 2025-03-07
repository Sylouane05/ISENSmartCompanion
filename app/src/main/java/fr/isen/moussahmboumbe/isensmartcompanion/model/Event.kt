package fr.isen.moussahmboumbe.isensmartcompanion.model

data class Event(
    val id: String = "",
    val category: String = "",
    val date: String = "",
    val description: String = "",
    val location: String = "",
    val title: String = "",
    var notification: Boolean = false // ✅ Champ ajouté (par défaut à false)
)
