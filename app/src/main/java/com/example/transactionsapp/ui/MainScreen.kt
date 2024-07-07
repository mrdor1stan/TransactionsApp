package com.example.transactionsapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.transactionsapp.data.Category
import com.example.transactionsapp.data.Transaction
import com.example.transactionsapp.ui.theme.TransactionsAppTheme
import java.util.Date
import java.util.UUID


@Composable
fun MainScreen(rate: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(text = rate)
    }
}

@Composable
fun TransactionListItem(transaction: Transaction, modifier: Modifier = Modifier) {
    Row() {
        if (transaction.category != null)
            Icon(
                painter = painterResource(id = transaction.category.icon), contentDescription = null,
                modifier.background(MaterialTheme.colorScheme.secondaryContainer).padding(16.dp)
            )
        else
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TransactionsAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TransactionListItem(
                Transaction(
                    UUID.randomUUID(),
                    -3454.5,
                    Date(2025, 12, 30, 14, 12, 16),
                    Category.Electronics
                )
            )
        }
    }
}