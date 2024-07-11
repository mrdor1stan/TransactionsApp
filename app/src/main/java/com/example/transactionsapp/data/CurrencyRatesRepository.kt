package com.example.transactionsapp.data

import com.example.transactionsapp.network.CurrencyApiService

interface CurrencyRatesRepository {
    suspend fun getBitcoinToDollarRate(): Double
}

class NetworkCurrencyRatesRepository(
    private val currencyApiService: CurrencyApiService,
) : CurrencyRatesRepository {
    override suspend fun getBitcoinToDollarRate(): Double =
        currencyApiService
            .getRates()
            .bpi.usd.rate
}
