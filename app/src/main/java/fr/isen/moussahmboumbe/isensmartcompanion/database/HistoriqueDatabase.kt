package fr.isen.moussahmboumbe.isensmartcompanion.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.isen.moussahmboumbe.isensmartcompanion.model.HistoriqueItem

@Database(entities = [HistoriqueItem::class], version = 1, exportSchema = false)
abstract class HistoriqueDatabase : RoomDatabase() {
    abstract fun historiqueDao(): HistoriqueDao

    companion object {
        @Volatile
        private var INSTANCE: HistoriqueDatabase? = null

        fun getDatabase(context: Context): HistoriqueDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoriqueDatabase::class.java,
                    "historique_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
