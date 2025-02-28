package fr.isen.moussahmboumbe.isensmartcompanion.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.isen.moussahmboumbe.isensmartcompanion.model.ChatMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(chatMessage: ChatMessage)

    @Query("SELECT * FROM chat_messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<ChatMessage>> // Flow permet la mise à jour en temps réel

    @Query("DELETE FROM chat_messages WHERE id = :id")
    suspend fun deleteMessage(id: Int)

    @Query("DELETE FROM chat_messages")
    suspend fun deleteAllMessages()
}
