package com.example.transactionsapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController

@Composable
fun TransactionsApp(
    viewModel: TransactionsViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {

}
