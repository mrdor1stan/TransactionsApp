package com.example.transactionsapp.ui

import androidx.compose.foundation.layout.fillMaxSize
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
fun TransactionsApp(navController: NavHostController = rememberNavController(),
                    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = TransactionScreen.Main.name, modifier = modifier) {
        composable(TransactionScreen.Main.name) {
            MainScreen(modifier = Modifier.fillMaxSize())
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
    ),    Transaction(
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
    ),    Transaction(
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
    ),    Transaction(
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
    ),    Transaction(
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
    ),
)