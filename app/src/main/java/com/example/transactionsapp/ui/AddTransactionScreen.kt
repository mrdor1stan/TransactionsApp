package com.example.transactionsapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.transactionsapp.R
import com.example.transactionsapp.data.Category
import com.example.transactionsapp.ui.utils.TransactionsAppPositioning
import kotlinx.coroutines.launch

@Composable
fun AddTransactionScreen(
    positioning: TransactionsAppPositioning,
    navigateBack: () -> Unit,
    viewModel: AddTransactionScreenViewModel = viewModel(factory = AddTransactionScreenViewModel.Factory),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.transactionUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    if (positioning == TransactionsAppPositioning.Vertical) {
        Column(
            modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AddTransactionForm(
                inputValue = uiState.amountInput,
                onInputValueChanged = { viewModel.updateInput(it) },
                isErrorInput = !viewModel.validateInput(),
                onCategorySelected = { viewModel.updateCategory(it) },
                modifier = Modifier.fillMaxSize(),
            )
            AddTransactionButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.insertTransaction()
                        navigateBack()
                    }
                },
                enabled = viewModel.validateInput(),
                modifier =
                    Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
            )
        }
    } else {
        Row(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(8.dp),
        ) {
            AddTransactionForm(
                inputValue = uiState.amountInput,
                onInputValueChanged = { viewModel.updateInput(it) },
                isErrorInput = !viewModel.validateInput(),
                onCategorySelected = { viewModel.updateCategory(it) },
                modifier =
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(3f),
            )
            Box(Modifier.weight(1f).fillMaxHeight()) {
                AddTransactionButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.insertTransaction()
                            navigateBack()
                        }
                    },
                    enabled = viewModel.validateInput(),
                    modifier =
                        Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterEnd),
                )
            }
        }
    }
}

@Composable
fun AddTransactionButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
    ) {
        Text(text = stringResource(id = R.string.add))
    }
}

@Composable
fun AddTransactionForm(
    inputValue: String,
    onInputValueChanged: (String) -> Unit,
    isErrorInput: Boolean,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = inputValue,
            onValueChange = onInputValueChanged,
            isError = isErrorInput,
            label = { Text(text = stringResource(id = R.string.enter_amount)) },
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
            modifier = Modifier.fillMaxWidth(),
        )
        Text(text = stringResource(id = R.string.choose_category))
        CategoryRadioButtonsList(
            options = Category.entries.toList(),
            onSelectionChanged = onCategorySelected,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun CategoryRadioButtonsList(
    options: List<Category>,
    onSelectionChanged: (Category) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var selectedValue by rememberSaveable { mutableStateOf<Category?>(null) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            options.forEach { item ->
                Row(
                    modifier =
                        Modifier.selectable(
                            selected = selectedValue == item,
                            onClick = {
                                selectedValue = item
                                onSelectionChanged(item)
                            },
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = selectedValue == item,
                        onClick = {
                            selectedValue = item
                            onSelectionChanged(item)
                        },
                    )
                    Text(item.name)
                }
            }
        }
    }
}
