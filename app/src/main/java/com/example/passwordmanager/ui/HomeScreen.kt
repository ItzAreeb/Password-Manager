package com.example.passwordmanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.passwordmanager.data.Account

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier, accounts: List<Account>) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

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
            shape = RoundedCornerShape(50.dp), // Fully rounded corners
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent, // Remove underline when focused
                unfocusedIndicatorColor = Color.Transparent, // Remove underline when not focused
                disabledIndicatorColor = Color.Transparent // Remove underline when disabled
            )
        )


        // Filtered Account List
        val filteredAccounts = accounts.filter {
            it.getName().contains(searchQuery.text, ignoreCase = true)
        }

        filteredAccounts.forEach { account ->
            AccountItem(account, context)
        }

        Spacer(modifier = Modifier.weight(1f)) // Pushes button to bottom

        FloatingAddButton { navController.navigate("createAccount") }
    }
}
