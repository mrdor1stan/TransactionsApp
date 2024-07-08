package com.example.transactionsapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.transactionsapp.TransactionsApplication
import com.example.transactionsapp.data.CurrencyRatesRepository
import com.example.transactionsapp.data.RequestStatus
import com.example.transactionsapp.data.Transaction
import com.example.transactionsapp.data.TransactionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDateTime
import java.util.UUID

data class MainScreenUiState(
    val bitcoinRate: RequestStatus,
    val balance: RequestStatus,
    val transactions: List<Transaction>,
    val topUpScreenRequired: Boolean = false
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

    private var _input = MutableStateFlow("")
    val input = _input.asStateFlow()
    val uiState = _uiState.asStateFlow()

    init {
        updateBitcoinToDollarRate()
        updateBalance()
        loadTransactions()
    }

    fun validateInput(): Boolean =
        (input.value.toDoubleOrNull() ?: -1.0) > 0.0

    fun updateInput(newInput: String) {
        _input.update { newInput }
    }

    private fun updateBalance() {
        viewModelScope.launch {
            transactionsRepository.getBalance()
                .collect { balance ->
                    _uiState.update { oldUiState ->
                        oldUiState.copy(balance = RequestStatus.Success(balance))
                    }
                }
        }
    }

    fun topUp() {
        if (!validateInput())
            return
        val amount = input.value.toDouble()
        val date = LocalDateTime.now()
        viewModelScope.launch {
            val transaction = Transaction(UUID.randomUUID(), amount, date, category = null)
            transactionsRepository.insertTransaction(transaction)
        }
    }

    fun loadTransactions() {
        viewModelScope.launch {
            transactionsRepository.getAllTransactions().collect { collectedTransactions ->
                        _uiState.update { oldUiState ->
                            oldUiState.copy(transactions = collectedTransactions)
                        }
                }
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

    fun requireTopUpScreen(enabled: Boolean) {
        _uiState.update { it.copy(topUpScreenRequired = enabled) }
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