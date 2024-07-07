package com.example.transactionsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.transactionsapp.TransactionsApplication
import com.example.transactionsapp.data.CurrencyRatesRepository
import com.example.transactionsapp.data.TransactionsRepository
import com.example.transactionsapp.network.NetworkRequestStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

data class MainScreenUiState(val bitcoinRate: String, val lastUpdate: String)

class TransactionsViewModel(
    val transactionsRepository: TransactionsRepository,
    val currencyRatesRepository: CurrencyRatesRepository
) : ViewModel() {

    private var _bitcoinToDollarRate =
        MutableStateFlow<NetworkRequestStatus>(NetworkRequestStatus.Loading)
    val bitcoinToDollarRate = _bitcoinToDollarRate.asStateFlow()

    init {
        updateBitcoinToDollarRate()
    }

    fun updateBitcoinToDollarRate() {
        viewModelScope.launch {
            try {
                val rate = currencyRatesRepository.getBitcoinToDollarRate()
                _bitcoinToDollarRate.update { NetworkRequestStatus.Success(rate) }
            } catch (e: IOException) {
                _bitcoinToDollarRate.update { NetworkRequestStatus.Error }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TransactionsApplication)
                val transactionsRepository: TransactionsRepository =
                    application.container.transactionsRepository
                val currencyRatesRepository: CurrencyRatesRepository =
                    application.container.currencyRatesRepository
                TransactionsViewModel(
                    transactionsRepository = transactionsRepository,
                    currencyRatesRepository = currencyRatesRepository
                )
            }
        }
    }
}