package com.example.transactionsapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

// The abstraction of facade that hides the details of implementation from clients
interface TransactionsRepository {
    suspend fun insertTransaction(transaction: Transaction)

    fun getTransactions(): PagingSource<Int, Transaction>

    fun getBalance(): Flow<Double>
}

// The concrete implementation of the facade, which can be swapped for any other one
class LocalTransactionRepository(
    private val transactionsDao: TransactionsDao,
) : TransactionsRepository {
    override suspend fun insertTransaction(transaction: Transaction) = transactionsDao.insert(transaction)

    override fun getTransactions() = transactionsDao.getTransactions()

    override fun getBalance(): Flow<Double> = transactionsDao.getBalance()
}

// The mock facade implementation to demonstrate the behaviour
class RemoteTransactionRepository : TransactionsRepository {
    override suspend fun insertTransaction(transaction: Transaction) {
        // imagine a network call here
    }

    override fun getTransactions(): PagingSource<Int, Transaction> {
        // imagine a network call here
        return object : PagingSource<Int, Transaction>() {
            override fun getRefreshKey(state: PagingState<Int, Transaction>): Int? = 5

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Transaction> = LoadResult.Page(listOf(), 1, 3)
        }
    }

    override fun getBalance(): Flow<Double> {
        // imagine a network call here
        return MutableStateFlow(0.0)
    }
}
