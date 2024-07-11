package com.example.transactionsapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.transactionsapp.ui.utils.TransactionsAppPositioning

enum class TransactionScreen {
    Main, AddTransaction
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TransactionsApp(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val windowWidthSizeClass = windowSizeClass.widthSizeClass
    val positioning =
        if (windowWidthSizeClass == WindowWidthSizeClass.Expanded)
            TransactionsAppPositioning.Horizontal
        else
            TransactionsAppPositioning.Vertical
    NavHost(
        navController = navController,
        startDestination = TransactionScreen.Main.name,
        modifier = modifier
    ) {
        composable(TransactionScreen.Main.name) {
            MainScreen(
                positioning = positioning,
                onAddTransactionClick = { navController.navigate(TransactionScreen.AddTransaction.name) },
                modifier = Modifier.fillMaxSize()
            )
        }

        composable(TransactionScreen.AddTransaction.name) {
            AddTransactionScreen(navigateBack = {
                navController.popBackStack(
                    route = TransactionScreen.Main.name,
                    inclusive = false
                )
            })
        }
    }

}
