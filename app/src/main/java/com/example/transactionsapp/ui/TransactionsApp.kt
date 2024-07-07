package com.example.transactionsapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.transactionsapp.data.Category
import com.example.transactionsapp.data.Transaction
import java.util.Date
import java.util.UUID

enum class TransactionScreen {
    Main, AddTransaction
}

@Composable
fun TransactionsApp(
    viewModel: TransactionsViewModel = viewModel(factory = TransactionsViewModel.Factory),
    navController: NavHostController = rememberNavController(),
) {
    val rate by viewModel.bitcoinToDollarRate.collectAsState()

    NavHost(navController = navController, startDestination = TransactionScreen.Main.name) {
        composable(TransactionScreen.Main.name) {

            val transactionsMockList =transactionsMockList


            MainScreen(rate.toString(),transactions = transactionsMockList, Modifier)
        }

        composable(TransactionScreen.AddTransaction.name) {

        }
    }

}



val transactionsMockList = listOf(
    Transaction(
        UUID.randomUUID(),
        -3454.5,
        Date(2025, 12, 30, 14, 12, 16),
        Category.Electronics
    ), Transaction(
        UUID.randomUUID(),
        -34344.5,
        Date(2025, 12, 30, 10, 11, 16),
        Category.Groceries
    ), Transaction(
        UUID.randomUUID(),
        -3454.5,
        Date(2025, 12, 30, 12, 14, 16),
        null
    ), Transaction(
        UUID.randomUUID(),
        -3454.5,
        Date(2025, 12, 30, 14, 12, 16),
        Category.Restaurant
    ), Transaction(
        UUID.randomUUID(),
        -3454.5,
        Date(2025, 12, 30, 14, 12, 16),
        Category.Other
    )
)