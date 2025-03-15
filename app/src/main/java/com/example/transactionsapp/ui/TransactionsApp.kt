package com.example.transactionsapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class TransactionScreen {
    Main,
    AddTransaction,
}

@Composable
fun TransactionsApp(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val windowWidthSizeClass = windowSizeClass.widthSizeClass
    val positioning =
        if (windowWidthSizeClass == WindowWidthSizeClass.Expanded) {
            TransactionsAppPositioning.Horizontal
        } else {
            TransactionsAppPositioning.Vertical
        }
    NavHost(
        navController = navController,
        startDestination = TransactionScreen.Main.name,
        modifier = modifier,
    ) {
        composable(TransactionScreen.Main.name) {
            MainScreen(
                positioning = positioning,
                onAddTransactionClick = { navController.navigate(TransactionScreen.AddTransaction.name) },
                modifier = Modifier.fillMaxSize(),
            )
        }

        composable(TransactionScreen.AddTransaction.name) {
            AddTransactionScreen(
                positioning = positioning,
                navigateBack = {
                    navController.popBackStack(
                        route = TransactionScreen.Main.name,
                        inclusive = false,
                    )
                },
            )
        }
    }
}
