package fr.isen.moussahmboumbe.isensmartcompanion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historique")
data class HistoriqueItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val reponse: String
)


