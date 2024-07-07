package com.example.transactionsapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.transactionsapp.R
import com.example.transactionsapp.data.Category
import com.example.transactionsapp.ui.theme.TransactionsAppTheme


@Composable
fun AddTransactionScreen(modifier: Modifier = Modifier) {
    Column (
        modifier
            .fillMaxSize()
            .padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = "", onValueChange = {}, label = { Text(text = stringResource(id = R.string.enter_amount)) },
            modifier = Modifier.fillMaxWidth())
        Text(text = stringResource(id = R.string.choose_category))
        CategoryRadioButtonsList(options = Category.values().toList(), modifier = Modifier.fillMaxWidth())
        Button(onClick = { /*TODO*/ }, ) {
            Text(text = stringResource(id = R.string.add))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview(){
    TransactionsAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AddTransactionScreen()
        }
    }
}


@Composable
fun CategoryRadioButtonsList(
    options: List<Category>,
    onSelectionChanged: (Category) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedValue by rememberSaveable { mutableStateOf<Category?>(null) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            options.forEach { item ->
                Row(
                    modifier = Modifier.selectable(
                        selected = selectedValue == item,
                        onClick = {
                            selectedValue = item
                            onSelectionChanged(item)
                        }
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedValue == item,
                        onClick = {
                            selectedValue = item
                            onSelectionChanged(item)
                        }
                    )
                    Text(item.name)
                }
            }
        }
    }
}