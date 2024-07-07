package com.example.transactionsapp.data


import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface TransactionsRepository {
    suspend fun insertTransaction(transaction: Transaction)
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransaction(id: UUID): Flow<Transaction>
    fun getTransactions(limit: Int, offset: Int): Flow<List<Transaction>>
    fun getBalance(): Flow<Double>
}

class LocalTransactionRepository(private val transactionsDao: TransactionsDao): TransactionsRepository {
    override suspend fun insertTransaction(transaction: Transaction) = transactionsDao.insert(transaction)

    override fun getAllTransactions(): Flow<List<Transaction>> = transactionsDao.getAllTransactions()

    override fun getTransaction(id: UUID): Flow<Transaction> = transactionsDao.getTransaction(id)

    override fun getTransactions(limit: Int, offset: Int): Flow<List<Transaction>> = transactionsDao.getTransactions(limit = limit, offset = offset)

    override fun getBalance(): Flow<Double> = transactionsDao.getBalance()

}