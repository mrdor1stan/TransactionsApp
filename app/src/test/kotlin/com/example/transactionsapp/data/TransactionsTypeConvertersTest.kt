package com.example.transactionsapp.data

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.Month

class TransactionsTypeConvertersTest {
    private val typeConverter = TransactionsTypeConverters()
    private val dateString = "2008-12-04T12:12:00"
    private val dateTime: LocalDateTime = LocalDateTime.of(2008, Month.DECEMBER, 4, 12, 12)

    @Test
    fun stringConvertsToDateCorrectly(){
        assertEquals(dateTime, typeConverter.toLocalDateTime(dateString))
    }

    @Test
    fun dateConvertsToStringCorrectly(){
        assertEquals(dateString, typeConverter.fromLocalDateTime(dateTime))
    }
}