package com.khoshrang.forexquiz.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DataStoreManager(val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        val premium = booleanPreferencesKey("premium")
    }

    suspend fun savetoDataStore(premium1: Boolean) {
        context.dataStore.edit {
            it[premium] = premium1
        }
    }

    suspend fun getFromDataStore() = context.dataStore.data.map {
        it[premium] ?: false
    }

    suspend fun saveGrade(key: String, grade: String) {
        val keycode = stringPreferencesKey(key)
        context.dataStore.edit {
            it[keycode] = grade
        }
    }

    suspend fun getGrade(key: String) = context.dataStore.data.map {
        val keycode = stringPreferencesKey(key)
        it[keycode] ?: "0"
    }

}