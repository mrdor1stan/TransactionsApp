package com.example.transactionsapp.data

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {
    suspend fun insertTransaction(transaction: Transaction)

    fun getTransactions(): PagingSource<Int, Transaction>

    fun getBalance(): Flow<Double>
}

class LocalTransactionRepository(
    private val transactionsDao: TransactionsDao,
) : TransactionsRepository {
    override suspend fun insertTransaction(transaction: Transaction) = transactionsDao.insert(transaction)

    override fun getTransactions() = transactionsDao.getTransactions()

    override fun getBalance(): Flow<Double> = transactionsDao.getBalance()
}
