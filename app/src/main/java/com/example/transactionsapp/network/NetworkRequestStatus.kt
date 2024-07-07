package com.example.transactionsapp.network

sealed interface NetworkRequestStatus {
    object Loading: NetworkRequestStatus
    object Error: NetworkRequestStatus
    data class Success(val response: Double): NetworkRequestStatus
}