package com.example.transactionsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.transactionsapp.TransactionsApplication
import com.example.transactionsapp.data.Category
import com.example.transactionsapp.data.Transaction
import com.example.transactionsapp.data.TransactionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.util.UUID

data class TransactionUiState(
    val amountInput: String = "",
    val category: Category? = null,
)

class AddTransactionScreenViewModel(
    val transactionsRepository: TransactionsRepository,
) : ViewModel() {
    private var _transactionUiState =
        MutableStateFlow(
            TransactionUiState(),
        )
    val transactionUiState = _transactionUiState.asStateFlow()

    fun validateInput(): Boolean =
        transactionUiState.value.let {
            (it.amountInput.toDoubleOrNull() ?: -1.0) > 0.0 && it.category != null
        }

    fun updateInput(newInput: String) {
        _transactionUiState.update { it.copy(amountInput = newInput) }
    }

    fun updateCategory(category: Category) {
        _transactionUiState.update { it.copy(category = category) }
    }

    suspend fun insertTransaction() {
        if (!validateInput() || transactionUiState.value.category == null) {
            return
        }
        val amount = -transactionUiState.value.amountInput.toDouble()
        val date = LocalDateTime.now()
        val transaction = Transaction(UUID.randomUUID(), amount, date, category = transactionUiState.value.category)
        transactionsRepository.insertTransaction(transaction)
    }

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as TransactionsApplication)
                    val transactionsRepository: TransactionsRepository =
                        application.container.transactionsRepository
                    AddTransactionScreenViewModel(
                        transactionsRepository = transactionsRepository,
                    )
                }
            }
    }
}
