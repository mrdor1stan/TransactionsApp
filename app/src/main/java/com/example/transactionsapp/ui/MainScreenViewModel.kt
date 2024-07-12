package com.example.transactionsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.transactionsapp.TransactionsApplication
import com.example.transactionsapp.data.RatesPreferencesRepository
import com.example.transactionsapp.data.RequestStatus
import com.example.transactionsapp.data.Transaction
import com.example.transactionsapp.data.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

data class MainScreenUiState(
    val bitcoinRate: Double?,
    val bitcoinRateUpdate: LocalDateTime? = null,
    val balance: RequestStatus,
    val topUpScreenRequired: Boolean = false,
)

private const val TRANSACTIONS_ON_PAGE = 20

class MainScreenViewModel(
    val transactionsRepository: TransactionsRepository,
    val ratesPreferencesRepository: RatesPreferencesRepository,
) : ViewModel() {
    private var _uiState =
        MutableStateFlow(
            MainScreenUiState(
                bitcoinRate = null,
                balance = RequestStatus.Loading,
            ),
        )

    private var _input = MutableStateFlow("")
    val input = _input.asStateFlow()
    val uiState = _uiState.asStateFlow()

    val transactions: Flow<PagingData<Transaction>> =
        Pager(PagingConfig(pageSize = TRANSACTIONS_ON_PAGE)) {
            transactionsRepository.getTransactions()
        }.flow.cachedIn(viewModelScope)

    val rate: Flow<Pair<Double?, LocalDateTime?>> = ratesPreferencesRepository.rate

    init {
        updateBitcoinToDollarRate()
        updateBalance()
    }

    fun validateInput(): Boolean = (input.value.toDoubleOrNull() ?: -1.0) > 0.0

    fun updateInput(newInput: String) {
        _input.update { newInput }
    }

    private fun updateBalance() {
        viewModelScope.launch {
            transactionsRepository
                .getBalance()
                .collect { balance ->
                    _uiState.update { oldUiState ->
                        oldUiState.copy(balance = RequestStatus.Success(balance))
                    }
                }
        }
    }

    fun topUp() {
        if (!validateInput()) {
            return
        }
        val amount = input.value.toDouble()
        val date = LocalDateTime.now()
        viewModelScope.launch {
            val transaction = Transaction(UUID.randomUUID(), amount, date, category = null)
            transactionsRepository.insertTransaction(transaction)
        }
    }

    fun updateBitcoinToDollarRate() {
        viewModelScope.launch {
            rate.collect { (rate, lastUpdate) ->
                _uiState.update { it.copy(bitcoinRate = rate, bitcoinRateUpdate = lastUpdate) }
            }
        }
    }

    fun requireTopUpScreen(enabled: Boolean) {
        _uiState.update { it.copy(topUpScreenRequired = enabled) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as TransactionsApplication)
                    val transactionsRepository: TransactionsRepository =
                        application.container.transactionsRepository
                    val ratesPreferencesRepository: RatesPreferencesRepository =
                        application.container.ratesPreferencesRepository
                    MainScreenViewModel(
                        transactionsRepository = transactionsRepository,
                        ratesPreferencesRepository = ratesPreferencesRepository,
                    )
                }
            }
    }
}
