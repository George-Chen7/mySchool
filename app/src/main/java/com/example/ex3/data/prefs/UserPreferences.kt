package com.example.ex3.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

private val KEY_USERNAME = stringPreferencesKey("username")
private val KEY_PASSWORD = stringPreferencesKey("password")
private val KEY_REMEMBER_PWD = booleanPreferencesKey("remember_password")
private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

class UserPreferences(private val context: Context) {

    // DataStore (modern)
    val usernameFlow: Flow<String> = context.dataStore.data.map { it[KEY_USERNAME] ?: "" }
    val rememberPasswordFlow: Flow<Boolean> = context.dataStore.data.map { it[KEY_REMEMBER_PWD] ?: false }
    val passwordFlow: Flow<String> = context.dataStore.data.map { it[KEY_PASSWORD] ?: "" }
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data.map { it[KEY_IS_LOGGED_IN] ?: false }

    suspend fun saveLoginInfo(username: String, password: String, remember: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USERNAME] = username
            prefs[KEY_REMEMBER_PWD] = remember
            if (remember) prefs[KEY_PASSWORD] = password else prefs.remove(KEY_PASSWORD)
            prefs[KEY_IS_LOGGED_IN] = true
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_IS_LOGGED_IN)
        }
    }

    // Raw SharedPreferences API (for comparison demo)
    private val rawPrefs: SharedPreferences =
        context.getSharedPreferences("raw_prefs", Context.MODE_PRIVATE)

    fun saveThemePreference(darkMode: Boolean) {
        rawPrefs.edit().putBoolean("dark_mode", darkMode).apply()
    }

    fun getThemePreference(): Boolean {
        return rawPrefs.getBoolean("dark_mode", false)
    }
}
