package com.example.passwordmanager.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.passwordmanager.data.Account

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier, accounts: MutableList<Account>) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var editingAccount by remember { mutableStateOf<Account?>(null) }

    Box {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Rounded Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search...") },
                singleLine = true,
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            // Filtered Account List
            val filteredAccounts = accounts.filter {
                it.getName().contains(searchQuery.text, ignoreCase = true)
            }

            filteredAccounts.forEach { account ->
                AccountItem(
                    account = account,
                    context = context,
                    onEdit = { accountToEdit ->
                        editingAccount = accountToEdit
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            FloatingAddButton { navController.navigate("createAccount") }
        }

        // Edit Dialog
        editingAccount?.let { account ->
            EditAccountDialog(
                account = account,
                onDismiss = { editingAccount = null },
                onSave = { updatedAccount ->
                    val index = accounts.indexOfFirst { it.getName() == account.getName() }
                    if (index != -1) {
                        accounts[index] = updatedAccount
                    }
                    editingAccount = null
                }
            )
        }
    }
}

@Composable
fun EditAccountDialog(
    account: Account,
    onDismiss: () -> Unit,
    onSave: (Account) -> Unit
) {
    var editedName by remember { mutableStateOf(account.getName()) }
    var editedPassword by remember { mutableStateOf(account.getPassword()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Account") },
        text = {
            Column {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Account Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = editedPassword,
                    onValueChange = { editedPassword = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedAccount = Account().apply {
                    setName(editedName)
                    setPassword(editedPassword)
                }
                onSave(updatedAccount)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}