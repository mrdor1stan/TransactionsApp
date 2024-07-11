package com.example.transactionsapp.data

sealed interface RequestStatus {
    object Loading : RequestStatus

    object Error : RequestStatus

    data class Success(
        val response: Double,
    ) : RequestStatus
}
