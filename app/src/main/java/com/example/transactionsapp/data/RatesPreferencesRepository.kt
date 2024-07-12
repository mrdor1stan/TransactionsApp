package com.example.transactionsapp.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.LocalDateTime

class RatesPreferencesRepository(
    private val dataStore: DataStore<Preferences>,
) {
    private companion object {
        val DOLLAR_RATE = doublePreferencesKey("usd_rate")
        val RATE_UPDATE_DATE_TIME = stringPreferencesKey("rate_update_date_time")
        const val TAG = "RatesPreferencesRepository"
    }

    suspend fun saveRatesPreferences(
        dollarRate: Double,
        rateUpdateDateTime: LocalDateTime,
    ) {
        val rateUpdateString = rateUpdateDateTime.toString()
        dataStore.edit { preferences ->
            preferences[DOLLAR_RATE] = dollarRate
            preferences[RATE_UPDATE_DATE_TIME] = rateUpdateString
        }
    }

    val rate: Flow<Pair<Double?, LocalDateTime?>> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    Log.e(TAG, "Error reading preferences", it)
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map { preferences ->
                preferences[DOLLAR_RATE] to LocalDateTime.parse(preferences[RATE_UPDATE_DATE_TIME])
            }
}
