package app.lenth.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class DevelopmentPreference(private val developmentDataStore: DataStore<Preferences>) {

    companion object {
        const val PREFERENCE_USER_LANGUAGE = "user_language"
        const val PREFERENCE_THEME_IS_DARK_MODE = "is_dark_mode"
    }

    fun getBooleanFlow(preferenceKey: String): Flow<Boolean?> = developmentDataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(preferenceKey)]
    }

    suspend fun getBooleanData(preferenceKey: String): Boolean? {
        val key = booleanPreferencesKey(preferenceKey)
        return developmentDataStore.data.firstOrNull()?.get(key)
    }

    fun getStringFlow(preferenceKey: String): Flow<String?> = developmentDataStore.data.map { preferences ->
        preferences[stringPreferencesKey(preferenceKey)]
    }

    suspend fun setStringData(preferenceKey: String, value: String?) {
        val key = stringPreferencesKey(preferenceKey)
        developmentDataStore.edit { preferences ->
            if(value == null) {
                preferences.clear()
                return@edit
            }
            preferences[key] = value
        }
    }

    fun getStringData(preferenceKey: String): String? = runBlocking {
        val key = stringPreferencesKey(preferenceKey)
        developmentDataStore.data.firstOrNull()?.get(key)
    }

    suspend fun setData(preferenceKey: String, value: Boolean) {
        val key = booleanPreferencesKey(preferenceKey)
        developmentDataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun setLongData(preferenceKey: String, value: Long) {
        val key = longPreferencesKey(preferenceKey)
        developmentDataStore.edit { preferences ->
            preferences[key] = value
        }
    }


    fun getDataString(preferenceKey: String): String? = runBlocking {
        val key = stringPreferencesKey(preferenceKey)
        developmentDataStore.data.firstOrNull()?.get(key)
    }


    fun getLongData(preferenceKey: String): Long? = runBlocking {
        val key = longPreferencesKey(preferenceKey)
        developmentDataStore.data.firstOrNull()?.get(key)
    }

    fun getData(preferenceKey: String): Boolean? = runBlocking {
        val key = booleanPreferencesKey(preferenceKey)
        developmentDataStore.data.firstOrNull()?.get(key)
    }


    fun getBooleanDataFlow(preferenceKey: String, ifNotAvailable: Boolean): Flow<Boolean> = developmentDataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(preferenceKey)] ?: ifNotAvailable
    }

    fun getLongDataFlow(preferenceKey: String, ifNotAvailable: Long): Flow<Long> = developmentDataStore.data.map { preferences ->
        preferences[longPreferencesKey(preferenceKey)] ?: ifNotAvailable
    }

    fun getStringDataFlow(preferenceKey: String, ifNotAvailable: String?): Flow<String?> = developmentDataStore.data.map { preferences ->
        preferences[stringPreferencesKey(preferenceKey)] ?: ifNotAvailable
    }
}