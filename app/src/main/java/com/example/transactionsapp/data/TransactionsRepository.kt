package com.example.transactionsapp.data


import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface TransactionsRepository {
    suspend fun insertTransaction(transaction: Transaction)
    fun getTransaction(id: UUID): Flow<Transaction>
    fun getTransactions(): PagingSource<Int, Transaction>
    fun getBalance(): Flow<Double>
}

class LocalTransactionRepository(private val transactionsDao: TransactionsDao) :
    TransactionsRepository {
    override suspend fun insertTransaction(transaction: Transaction) =
        transactionsDao.insert(transaction)

    override fun getTransaction(id: UUID): Flow<Transaction> = transactionsDao.getTransaction(id)

    override fun getTransactions() = transactionsDao.getTransactions()

    override fun getBalance(): Flow<Double> = transactionsDao.getBalance()

}