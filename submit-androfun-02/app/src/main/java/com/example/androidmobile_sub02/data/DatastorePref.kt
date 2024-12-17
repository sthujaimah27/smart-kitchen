package com.example.androidmobile_sub02.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class DatastorePref private constructor(private val dataStore: DataStore<Preferences>){

    private val TEHEME_KEY = booleanPreferencesKey(KEY_OF_THEME)

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[TEHEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[TEHEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DatastorePref? = null

        const val KEY_OF_THEME = "theme_setting"

        fun getInstance(dataStore: DataStore<Preferences>): DatastorePref {
            return INSTANCE ?: synchronized(this) {
                val instance = DatastorePref(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}