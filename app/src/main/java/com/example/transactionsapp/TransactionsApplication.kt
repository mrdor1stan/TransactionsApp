package com.example.transactionsapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.transactionsapp.di.AppContainer
import com.example.transactionsapp.di.DefaultAppContainer

private const val RATES_PREFERENCE_NAME = "rates_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = RATES_PREFERENCE_NAME,
)

class TransactionsApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this, dataStore)
    }
}
