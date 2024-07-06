package com.example.transactionsapp.data

import androidx.annotation.DrawableRes
import androidx.room.Entity
import com.example.transactionsapp.R
import java.util.Date
import java.util.UUID

enum class Category(@DrawableRes val icon: Int){
    Taxi(R.drawable.ic_taxi),
    Groceries(R.drawable.ic_groceries),
    Electronics(R.drawable.ic_electronics),
    Restaurant(R.drawable.ic_restaurant),
    Other(R.drawable.ic_other)
}

@Entity(tableName = "transactions")
data class Transaction(val id: UUID, val amount: Double, val dateTime: Date, val category: Category?)
