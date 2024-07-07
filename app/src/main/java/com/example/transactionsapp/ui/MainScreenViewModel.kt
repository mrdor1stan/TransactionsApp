package com.example.transactionsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.transactionsapp.TransactionsApplication
import com.example.transactionsapp.data.CurrencyRatesRepository
import com.example.transactionsapp.data.Transaction
import com.example.transactionsapp.data.TransactionsRepository
import com.example.transactionsapp.data.RequestStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

data class MainScreenUiState(
    val bitcoinRate: RequestStatus,
    val balance: RequestStatus,
    val transactions: List<Transaction>
)

private val TRANSACTIONS_ON_PAGE = 20

class MainScreenViewModel(
    val transactionsRepository: TransactionsRepository,
    val currencyRatesRepository: CurrencyRatesRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(
        MainScreenUiState(
            bitcoinRate = RequestStatus.Loading,
            balance = RequestStatus.Loading,
            transactions = listOf()
        )
    )
    val uiState = _uiState.asStateFlow()
    var transactionPage = 0

    init {
        updateBitcoinToDollarRate()
        updateBalance()
        updateTransactions()
    }

    private fun updateBalance() {
        viewModelScope.launch {
            transactionsRepository.getBalance()
                .collect { balance ->
                    _uiState.update { oldUiState ->
                        oldUiState.copy(balance = RequestStatus.Success(balance))
                    }
                }
            transactionPage++
        }
    }

    fun updateTransactions() {
        viewModelScope.launch {
            transactionsRepository.getTransactions(TRANSACTIONS_ON_PAGE, transactionPage)
                .collect { collectedTransactions ->
                    _uiState.update { oldUiState ->
                        oldUiState.copy(transactions = oldUiState.transactions + collectedTransactions)
                    }
                }
            transactionPage++
        }
    }

    fun updateBitcoinToDollarRate() {
        viewModelScope.launch {
            try {
                val rate = currencyRatesRepository.getBitcoinToDollarRate()
                _uiState.update { it.copy(bitcoinRate = RequestStatus.Success(rate)) }
            } catch (e: IOException) {
                _uiState.update { it.copy(bitcoinRate = RequestStatus.Error) }
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
                MainScreenViewModel(
                    transactionsRepository = transactionsRepository,
                    currencyRatesRepository = currencyRatesRepository
                )
            }
        }
    }
}