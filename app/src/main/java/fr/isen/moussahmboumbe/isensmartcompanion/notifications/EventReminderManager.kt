package fr.isen.moussahmboumbe.isensmartcompanion.notifications

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("event_prefs")

class EventReminderManager(private val context: Context) {

    companion object {
        private const val EVENT_KEY_PREFIX = "event_"
    }

    fun isEventReminderSet(eventId: String): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(EVENT_KEY_PREFIX + eventId)] ?: false
        }
    }

    suspend fun toggleEventReminder(eventId: String) {
        context.dataStore.edit { preferences ->
            val key = booleanPreferencesKey(EVENT_KEY_PREFIX + eventId)
            val currentValue = preferences[key] ?: false
            preferences[key] = !currentValue
        }
    }
}
