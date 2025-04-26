package com.example.passwordmanager.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier, accounts: MutableList<Account>) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var editingAccount by remember { mutableStateOf<Account?>(null) }
    var deletingAccount by remember { mutableStateOf<Account?>(null) }
    var navigationInProgress by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun handleNavigateToCreate() {
        if (navigationInProgress) return

        navigationInProgress = true
        coroutineScope.launch {
            try {
                navController.navigate("createAccount") {
                    launchSingleTop = true
                    popUpTo(navController.graph.startDestinationId)
                    restoreState = true
                }
            } finally {
                // Ensure we always reset after navigation attempt
                delay(300) // Minimum time between navigations
                navigationInProgress = false
            }
        }
    }

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

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)  // Takes remaining space
            ) {
                items(filteredAccounts) { account ->
                    AccountItem(
                        account = account,
                        context = context,
                        onEdit = { accountToEdit ->
                            editingAccount = accountToEdit
                        },
                        onDelete = { accountToDelete ->
                            deletingAccount = accountToDelete
                        }
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp, end = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingAddButton(
                onClick = { handleNavigateToCreate() },
                enabled = !navigationInProgress
            )
        }

        // Edit Dialog
        editingAccount?.let { account ->
            EditAccountDialog(
                account = account,
                onDismiss = { editingAccount = null },
                onSave = {
                    editingAccount = null
                }
            )
        }
        deletingAccount?.let { account ->
            DeleteAccountDialog(
                account = account,
                onDismiss = { deletingAccount = null },
                onDelete = {
                    val index = accounts.indexOfFirst { it.getName() == account.getName() }
                    if (index != -1) {
                        accounts.removeAt(index)
                    }
                    deletingAccount = null
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
                account.setName(editedName)
                account.setPassword(editedPassword)
                onSave(account)
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

@Composable
fun DeleteAccountDialog(
    account: Account,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete Account",
                color = MaterialTheme.colorScheme.error
            )
        },
        text = {
            Column {
                Text("Are you sure you want to delete this account?")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = account.getName(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "This action cannot be undone",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}