package com.example.transactionsapp.network

import retrofit2.http.GET


interface CurrencyApiService {
    @GET("currentprice.json")
    suspend fun getRates(): CurrencyRatesResponse
}
