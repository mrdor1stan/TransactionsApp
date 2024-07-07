package com.example.transactionsapp.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
            MainScreen(rate.toString(), Modifier)
        }

        composable(TransactionScreen.AddTransaction.name) {

        }
    }

}
