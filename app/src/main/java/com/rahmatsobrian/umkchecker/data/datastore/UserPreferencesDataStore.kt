package com.rahmatsobrian.umkchecker.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.rahmatsobrian.umkchecker.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = Constants.PREFERENCES_NAME)

/**
 * Persists lightweight user preferences such as the Dynamic Color (Material You)
 * toggle. Kept intentionally small — most app state lives in Room.
 */
@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object Keys {
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color_enabled")
    }

    val isDynamicColorEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.DYNAMIC_COLOR] ?: true
    }

    suspend fun setDynamicColorEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.DYNAMIC_COLOR] = enabled }
    }
}
