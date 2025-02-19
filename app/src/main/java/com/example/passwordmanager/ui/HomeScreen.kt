package com.example.passwordmanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.passwordmanager.data.Account

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier, accounts: List<Account>) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Password Manager",
            fontSize = 30.sp,
            modifier = Modifier.padding(16.dp)
        )

        accounts.forEach { account ->
            AccountItem(account)
        }

        Spacer(modifier = Modifier.weight(1f)) // Pushes button to bottom

        FloatingAddButton { navController.navigate("createAccount") }
    }
}
