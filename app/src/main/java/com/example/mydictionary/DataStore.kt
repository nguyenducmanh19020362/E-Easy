package com.example.mydictionary

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class DataStore(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val STATE = booleanPreferencesKey("state")
        private val NOTE = stringPreferencesKey("note")
        private val TIME = stringPreferencesKey("time")
    }
    suspend fun note(state: Boolean, note: String, time: String): Int {
        var value = 1
        dataStore.edit { preferences ->
            preferences[STATE] = state
            preferences[NOTE] = note
            preferences[TIME] = time
            runBlocking {
                value = 2
            }
        }
        return value
    }

    suspend fun readNote(): String{
        val value: Flow<String> = dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[NOTE] ?: ""
            }
        return value .first()
    }

    suspend fun readState(): Boolean{
        val exampleCounterFlow: Flow<Boolean> = dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[STATE] ?: false
            }
        return exampleCounterFlow.first()
    }

    suspend fun readTime(): String{
        val value: Flow<String> = dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[TIME] ?: ""
            }
        return value .first()
    }

    suspend fun clear(): Unit{
    }
}