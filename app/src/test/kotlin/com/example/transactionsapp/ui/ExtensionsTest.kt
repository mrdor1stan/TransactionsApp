package com.example.transactionsapp.ui.utils

import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ExtensionsTest {

    @Test
    fun toFormattedNumber() {
        assertEquals("34,43", 34.42789824.toFormattedNumber(2))
        assertEquals("34,428", 34.42789824.toFormattedNumber(3))
    }

    @Test
    fun toFormattedDate_onTodayDate() {
        val today = LocalDate.now()
        assertEquals("Today", today.toFormattedDate())

    }

    @Test
    fun toFormattedDate_onYesterdayDate() {
        val yesterday = LocalDate.now().minusDays(1)
        assertEquals("Yesterday", yesterday.toFormattedDate())
    }

    @Test
    fun toFormattedDate_onRandomDate() {
        val date = LocalDate.of(2023,4,28)
        assertEquals(date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")), date.toFormattedDate())
    }

    @Test
    fun toFormattedTime(){
        val time = LocalDateTime.now()
        assertEquals("${time.hour}:${time.minute}", time.toFormattedTime())
    }
}