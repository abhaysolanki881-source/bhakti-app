package com.example.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "bhakti_settings")

class PreferencesRepository(private val context: Context) {

    companion object {
        val KEY_LANGUAGE = stringPreferencesKey("app_language")
        val KEY_DARK_THEME = booleanPreferencesKey("dark_theme")
        val KEY_SELECTED_CITY = stringPreferencesKey("selected_city")
        val KEY_FONT_SIZE = floatPreferencesKey("font_size_multiplier")
        val KEY_MORNING_REMINDER = booleanPreferencesKey("morning_reminder")
        val KEY_EVENING_REMINDER = booleanPreferencesKey("evening_reminder")
    }

    val languageFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_LANGUAGE] ?: "gu"
    }

    val darkThemeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_DARK_THEME] ?: false
    }

    val selectedCityFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_SELECTED_CITY] ?: "અમદાવાદ"
    }

    val fontSizeFlow: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[KEY_FONT_SIZE] ?: 1.0f
    }

    val morningReminderFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_MORNING_REMINDER] ?: true
    }

    val eveningReminderFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_EVENING_REMINDER] ?: true
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = language
        }
    }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_DARK_THEME] = isDark
        }
    }

    suspend fun setSelectedCity(cityName: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SELECTED_CITY] = cityName
        }
    }

    suspend fun setFontSize(multiplier: Float) {
        context.dataStore.edit { preferences ->
            preferences[KEY_FONT_SIZE] = multiplier
        }
    }

    suspend fun setMorningReminder(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_MORNING_REMINDER] = enabled
        }
    }

    suspend fun setEveningReminder(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_EVENING_REMINDER] = enabled
        }
    }
}
