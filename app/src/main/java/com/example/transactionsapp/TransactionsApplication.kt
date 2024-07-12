package com.example.transactionsapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.transactionsapp.data.CurrencyRatesRepository
import com.example.transactionsapp.data.RequestStatus
import com.example.transactionsapp.di.AppContainer
import com.example.transactionsapp.di.DefaultAppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private const val RATES_PREFERENCE_NAME = "rates_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = RATES_PREFERENCE_NAME,
)

private fun getLifecycleEventObserver(
    onAppForegrounded: () -> Unit,
    onAppBackgrounded: () -> Unit,
) = LifecycleEventObserver { _, event ->
    if (event == Lifecycle.Event.ON_STOP) {
        onAppBackgrounded()
    } else if (event == Lifecycle.Event.ON_START) {
        onAppForegrounded()
    }
}

class TransactionsApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this, dataStore)
        var coroutineScope: CoroutineScope? = null

        ProcessLifecycleOwner.get().lifecycle.addObserver(
            getLifecycleEventObserver(
                onAppBackgrounded = {
                    coroutineScope?.cancel()
                },
                onAppForegrounded = {
                    coroutineScope = CoroutineScope(Dispatchers.IO)
                    coroutineScope!!.launch {
                        container.ratesPreferencesRepository.rate.collect { (_, lastUpdate) ->
                            val currentDateTime = LocalDateTime.now()
                            // if there were no updates prior to this or 1 hour passed
                            if (
                                lastUpdate != null ||
                                ChronoUnit.HOURS.between(lastUpdate, currentDateTime) < 1
                            ) {
                                // make a request
                                val rateResult = getBitcoinToDollarRate(container.currencyRatesRepository)
                                // save its result if it's successful
                                if (rateResult is RequestStatus.Success) {
                                    container.ratesPreferencesRepository.saveRatesPreferences(
                                        dollarRate = rateResult.response,
                                        rateUpdateDateTime = currentDateTime,
                                    )
                                }
                            }
                        }
                    }
                },
            ),
        )
    }

    private suspend fun getBitcoinToDollarRate(currencyRatesRepository: CurrencyRatesRepository): RequestStatus {
        try {
            val rate = currencyRatesRepository.getBitcoinToDollarRate()
            return RequestStatus.Success(rate)
        } catch (e: IOException) {
            return RequestStatus.Error
        }
    }
}
