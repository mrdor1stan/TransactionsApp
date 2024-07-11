package com.example.transactionsapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.transactionsapp.R
import com.example.transactionsapp.data.RequestStatus
import com.example.transactionsapp.data.Transaction
import com.example.transactionsapp.ui.utils.TransactionsAppPositioning
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Double.toFormattedNumber(decimal: Int = 2) = String.format("%.${decimal}f", this)
fun LocalDate.toFormattedDate(): String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    return when (this) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
    }
}

fun LocalDateTime.toFormattedTime(): String = format(DateTimeFormatter.ofPattern("hh:mm"))
fun Transaction.getFormattedDate() = dateTime.toLocalDate().toFormattedDate()


@Composable
fun MainScreen(
    positioning: TransactionsAppPositioning,
    onAddTransactionClick: () -> Unit,
    viewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModel.Factory),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val transactionsLazyPagingItems: LazyPagingItems<Transaction> =
        viewModel.transactions.collectAsLazyPagingItems()
    val rateString = when (val rate = uiState.bitcoinRate) {
        is RequestStatus.Success -> rate.response.toFormattedNumber()
        else -> "---"
    }
    val balanceString = when (val balance = uiState.balance) {
        is RequestStatus.Success -> balance.response.toFormattedNumber()
        else -> "----"
    }


    if (positioning == TransactionsAppPositioning.Vertical) {
        Column(
            modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            BalanceComposable(
                rate = rateString,
                balance = balanceString, onAddButtonClicked = {
                    viewModel.requireTopUpScreen(true)
                },
                onAddTransaction = onAddTransactionClick,
                modifier = Modifier
                    .padding(horizontal = 8.dp).padding(bottom = 8.dp).fillMaxWidth().wrapContentHeight()
            )
            Divider()
            TransactionListOrPlaceholder(transactionsLazyPagingItems = transactionsLazyPagingItems)
        }
    } else {
        Row(
            modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            BalanceComposable(
                rate = rateString,
                balance = balanceString, onAddButtonClicked = {
                    viewModel.requireTopUpScreen(true)
                },
                onAddTransaction = onAddTransactionClick,
                modifier = modifier.padding(8.dp).weight(2f)
            )
            TransactionListOrPlaceholder(transactionsLazyPagingItems = transactionsLazyPagingItems, modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp).weight(3f))
        }
    }

    val dialogInput by viewModel.input.collectAsState()
    if (uiState.topUpScreenRequired)
        TopUpDialog(value = dialogInput,
            onValueChange = { viewModel.updateInput(it) },
            isInputValid = viewModel.validateInput(),
            onTopUp = {
                viewModel.topUp()
                viewModel.requireTopUpScreen(false)
                viewModel.updateInput("")
            },
            onDismiss = {
                viewModel.updateInput("")
                viewModel.requireTopUpScreen(false)
            })
}

@Composable
fun TransactionListOrPlaceholder(
    transactionsLazyPagingItems: LazyPagingItems<Transaction>,
    modifier: Modifier = Modifier
) {
    if (transactionsLazyPagingItems.itemCount == 0) {
        Box(modifier) {
            Text(
                text = stringResource(id = R.string.empty_transactions_placeholder),
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
            )
        }
    } else
        TransactionList(transactionsLazyPagingItems, modifier = modifier.fillMaxSize())
}

@Composable
fun BalanceComposable(
    rate: String,
    balance: String,
    onAddButtonClicked: () -> Unit,
    onAddTransaction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = rate, modifier = Modifier.align(Alignment.End))
        Text(
            text = stringResource(id = R.string.balance),
            style = MaterialTheme.typography.headlineMedium
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = balance, style = MaterialTheme.typography.displayMedium)
            Button(onClick = { onAddButtonClicked() }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.top_up),
                    modifier = Modifier
                        .padding(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.extraLarge
                        ),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Button(onClick = onAddTransaction) {
            Text(
                text = stringResource(id = R.string.add_transaction),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun TransactionList(transactions: LazyPagingItems<Transaction>, modifier: Modifier = Modifier) {
    LazyColumn(modifier) {
        items(
            count = transactions.itemCount,
            key = transactions.itemKey { transaction -> transaction.id },
            contentType = transactions.itemContentType { "transaction" }
        ) { index: Int ->
            val transaction = transactions[index]
            //If it's the first item on the list or the date doesn't match the previous item's date, then it needs a new header
            val isStartOfGroup =
                index == 0 || transactions[index]?.dateTime?.toLocalDate() != transactions[index - 1]?.dateTime?.toLocalDate()
            transaction?.let {
                if (isStartOfGroup) {
                    Text(
                        text = it.getFormattedDate(),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                TransactionListItem(
                    transaction = it,
                    addDivider = !isStartOfGroup
                )
            }
        }
    }

}

@Composable
fun TransactionListItem(
    transaction: Transaction,
    addDivider: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (addDivider) Divider()
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
                    text = transaction.amount.toFormattedNumber(),
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
                    Text(
                        text = transaction.dateTime.toFormattedTime(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

        }
    }
}


@Composable
private fun TopUpDialog(
    isInputValid: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    onTopUp: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = onDismiss,

        title = { Text(stringResource(R.string.top_up)) },
        text = {
            OutlinedTextField(
                value = value, onValueChange = onValueChange, isError = !isInputValid, label = {
                    Text(stringResource(R.string.enter_amount))
                }, keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onTopUp, enabled = isInputValid) {
                Text(stringResource(R.string.top_up))
            }
        })
}

