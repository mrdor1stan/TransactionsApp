package com.example.transactionsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.transactionsapp.TransactionsApplication
import com.example.transactionsapp.data.Transaction
import com.example.transactionsapp.data.TransactionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.util.UUID

class AddTransactionScreenViewModel(
    val transactionsRepository: TransactionsRepository
) : ViewModel() {

    private var _transaction = MutableStateFlow(
        Transaction(id = UUID.randomUUID(),
            amount = 0.0,
            dateTime = LocalDateTime.now(),
            category = null
        )
    )
    val transaction = _transaction.asStateFlow()

    private var _input = MutableStateFlow("")
    val input = _input.asStateFlow()

    fun validateInput(): Boolean =
        (input.value.toDoubleOrNull() ?: -1.0) > 0.0

    fun updateInput(newInput: String) {
        _input.update { newInput }
    }


    suspend fun insertTransaction() {
        if (!validateInput() || transaction.value.category == null)
            return
        val amount = -input.value.toDouble()
        val date = LocalDateTime.now()
        val transaction = Transaction(UUID.randomUUID(), amount, date, category = null)
        transactionsRepository.insertTransaction(transaction)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TransactionsApplication)
                val transactionsRepository: TransactionsRepository =
                    application.container.transactionsRepository
                AddTransactionScreenViewModel(
                    transactionsRepository = transactionsRepository
                )
            }
        }
    }
}