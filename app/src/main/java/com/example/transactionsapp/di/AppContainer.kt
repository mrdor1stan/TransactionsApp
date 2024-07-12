package com.example.transactionsapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.transactionsapp.data.CurrencyRatesRepository
import com.example.transactionsapp.data.LocalTransactionRepository
import com.example.transactionsapp.data.NetworkCurrencyRatesRepository
import com.example.transactionsapp.data.RatesPreferencesRepository
import com.example.transactionsapp.data.TransactionsDatabase
import com.example.transactionsapp.data.TransactionsRepository
import com.example.transactionsapp.network.CurrencyApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val transactionsRepository: TransactionsRepository
    val currencyRatesRepository: CurrencyRatesRepository
    val ratesPreferencesRepository: RatesPreferencesRepository
}

class DefaultAppContainer(
    private val context: Context,
    private val dataStore: DataStore<Preferences>,
) : AppContainer {
    private val ratesPreferencesName = "rates_preferences"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = ratesPreferencesName,
    )
    private val baseUrl = "https://api.coindesk.com/v1/bpi/"
    private val retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()

    private val retrofitService: CurrencyApiService by lazy {
        retrofit.create(CurrencyApiService::class.java)
    }

    override val transactionsRepository: TransactionsRepository by lazy {
        LocalTransactionRepository(TransactionsDatabase.getDatabase(context).transactionDao())
    }

    override val currencyRatesRepository: CurrencyRatesRepository by lazy {
        NetworkCurrencyRatesRepository(retrofitService)
    }
    override val ratesPreferencesRepository: RatesPreferencesRepository by lazy {
        RatesPreferencesRepository(dataStore = dataStore)
    }
}
