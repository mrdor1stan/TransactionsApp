package com.example.transactionsapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
fun MainScreen(rate: String, transactions: List<Transaction>, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(text = rate)
        TransactionList(transactions)
    }
}

@Composable
fun TransactionList(transactions: List<Transaction>, modifier: Modifier = Modifier) {
    LazyColumn (modifier) {
        val transactionGroups = transactions.sortedByDescending(Transaction::dateTime)
            .groupBy(Transaction::dateTime)

        for ((date, groupTransactions) in transactionGroups) {
            item {
                Text(text = date.toString(), style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(vertical = 8.dp))
            }
            itemsIndexed(items = groupTransactions) {index, item ->
                TransactionListItem(transaction = item, addDivider = index != groupTransactions.size-1)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun TransactionListItem(transaction: Transaction, addDivider: Boolean = true, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            val transactionCategoryName = transaction.category?.name ?: "Balance correction"

            val iconModifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)

            if (transaction.category != null)
                Icon(
                    painter = painterResource(id = transaction.category.icon),
                    contentDescription = null,
                    modifier = iconModifier
                )
            else
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = iconModifier
                )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = transaction.amount.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = transactionCategoryName,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(text = "12:15", style = MaterialTheme.typography.bodyMedium)
                }
            }

        }
        if(addDivider) Divider()
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TransactionsAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            TransactionList(transactions = transactionsMockList)
        }
    }
}

