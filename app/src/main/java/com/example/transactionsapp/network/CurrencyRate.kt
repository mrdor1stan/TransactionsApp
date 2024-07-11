package com.example.transactionsapp.network

import com.google.gson.annotations.SerializedName

data class CurrencyRatesResponse(
    val time: TimeResponse,
    val disclaimer: String,
    val chartName: String,
    var bpi: RatesPricesResponse,
)

data class TimeResponse(
    val updated: String,
    @SerializedName("updatedISO") val updatedIso: String,
    @SerializedName("updateduk") val updatedUk: String,
)

data class RatesPricesResponse(
    @SerializedName("USD") val usd: CurrencyRate,
    @SerializedName("GBP") val gbp: CurrencyRate,
    @SerializedName("EUR") val eur: CurrencyRate,
)

data class CurrencyRate(
    val code: String,
    val symbol: String,
    @SerializedName("rate") val rateString: String,
    val description: String,
    @SerializedName("rate_float") val rate: Double,
)
