package com.example.transactionsapp.data

sealed interface RequestStatus {
    data object Loading : RequestStatus

    data object Error : RequestStatus

    data class Success(
        val response: Double,
    ) : RequestStatus
}
