package fr.isen.moussahmboumbe.isensmartcompanion.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.isen.moussahmboumbe.isensmartcompanion.model.HistoriqueItem
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoriqueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistorique(historiqueItem: HistoriqueItem)

    @Query("SELECT * FROM historique ORDER BY id DESC")
    fun getAllHistorique(): Flow<List<HistoriqueItem>>

    @Delete
    suspend fun deleteHistorique(historiqueItem: HistoriqueItem)

    @Query("DELETE FROM historique")
    suspend fun clearHistorique()
}
