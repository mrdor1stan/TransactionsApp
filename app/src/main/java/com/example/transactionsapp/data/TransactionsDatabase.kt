package com.example.transactionsapp.data

import android.content.Context
import androidx.paging.PagingSource
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
@TypeConverters(TransactionsTypeConverters::class)
abstract class TransactionsDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionsDao

    companion object {
        @Volatile
        private var instance: TransactionsDatabase? = null
        private const val DATABASE_NAME = "transactions_db"

        fun getDatabase(context: Context): TransactionsDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return instance ?: synchronized(this) {
                Room
                    .databaseBuilder(context, TransactionsDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}

@Dao
interface TransactionsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY dateTime DESC")
    fun getTransactions(): PagingSource<Int, Transaction>

    @Query("SELECT SUM(amount) FROM transactions")
    fun getBalance(): Flow<Double>
}

@TypeConverters
class TransactionsTypeConverters {
    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime): String = date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    @TypeConverter
    fun toLocalDateTime(dateString: String): LocalDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}
