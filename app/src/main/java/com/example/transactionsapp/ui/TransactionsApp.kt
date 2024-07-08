package com.example.transactionsapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class TransactionScreen {
    Main, AddTransaction
}

@Composable
fun TransactionsApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = TransactionScreen.Main.name,
        modifier = modifier
    ) {
        composable(TransactionScreen.Main.name) {
            MainScreen(
                onAddTransactionClick = { navController.navigate(TransactionScreen.AddTransaction.name) },
                modifier = Modifier.fillMaxSize()
            )
        }

        composable(TransactionScreen.AddTransaction.name) {
            AddTransactionScreen( navigateBack = {navController.popBackStack(route = TransactionScreen.Main.name, inclusive = false)})
        }
    }

}
