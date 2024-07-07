package com.example.transactionsapp.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow
import java.util.Date
import java.util.UUID

@Database(entities = [Transaction::class], version=1)
@TypeConverters(TransactionsTypeConverters::class)
abstract class TransactionsDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionsDao

    companion object {
        @Volatile
        private var Instance: TransactionsDatabase? = null
        private const val DATABASE_NAME = "transactions_db"

        fun getDatabase(context: Context): TransactionsDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TransactionsDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

@Dao
interface TransactionsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transaction: Transaction)
    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<Transaction>>
    @Query("SELECT * FROM transactions WHERE id=:id")
    fun getTransaction(id: UUID): Flow<Transaction>
    @Query("SELECT * FROM transactions ORDER BY dateTime DESC LIMIT :limit OFFSET :offset")
    fun getTransactions(limit: Int, offset: Int): Flow<List<Transaction>>
    @Query("SELECT SUM(amount) FROM transactions")
    fun getBalance(): Flow<Double>
}

class TransactionsTypeConverters {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }
}