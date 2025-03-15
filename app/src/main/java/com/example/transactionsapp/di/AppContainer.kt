package com.example.transactionsapp.di

import android.content.Context
import com.example.transactionsapp.data.LocalTransactionRepository
import com.example.transactionsapp.data.TransactionsDatabase
import com.example.transactionsapp.data.TransactionsRepository

interface AppContainer {
    val transactionsRepository: TransactionsRepository
}

class DefaultAppContainer(
    private val context: Context,
) : AppContainer {
    override val transactionsRepository: TransactionsRepository by lazy {
        LocalTransactionRepository(TransactionsDatabase.getDatabase(context).transactionDao())
        // RemoteTransactionRepository()
    }
}
