package com.example.transactionsapp.ui

import com.example.transactionsapp.data.Transaction
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class TransactionsAppPositioning {
    Horizontal,
    Vertical,
}

fun Double.toFormattedNumber(decimal: Int = 2) = String.format("%.${decimal}f", this)

fun LocalDate.toFormattedDate(): String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    return when (this) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
    }
}

fun LocalDateTime.toFormattedTime(): String = format(DateTimeFormatter.ofPattern("hh:mm"))

fun Transaction.getFormattedDate() = dateTime.toLocalDate().toFormattedDate()
