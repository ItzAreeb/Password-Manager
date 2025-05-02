package com.example.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passwordmanager.data.Account
import com.example.passwordmanager.data.EncryptionHelper
import com.example.passwordmanager.ui.CreateAccountScreen
import com.example.passwordmanager.ui.HomeScreen
import com.example.passwordmanager.ui.theme.PasswordManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PasswordManagerTheme {
                val navController = rememberNavController()
                val accounts = remember { mutableStateListOf<Account>() }
                LaunchedEffect(Unit) {
                    val loadedAccounts = EncryptionHelper.loadAccounts(this@MainActivity)
                    accounts.addAll(loadedAccounts)
                }

                // Save accounts when they change
                DisposableEffect(accounts) {
                    onDispose {
                        EncryptionHelper.saveAccounts(this@MainActivity, accounts)
                    }
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController, startDestination = "home") {
                        composable("home") { HomeScreen(navController, accounts) }
                        composable("createAccount") { CreateAccountScreen(navController, Modifier, accounts) }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    PasswordManagerTheme {
        val navController = rememberNavController()
        val accounts = remember { mutableStateListOf<Account>() }

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(navController, startDestination = "home") {
                composable("home") { HomeScreen(navController, accounts) }
                composable("createAccount") { CreateAccountScreen(navController, Modifier.padding(innerPadding), accounts) }
            }
        }
    }
}
