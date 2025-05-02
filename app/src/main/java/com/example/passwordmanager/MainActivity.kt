package com.example.passwordmanager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passwordmanager.data.Account
import com.example.passwordmanager.data.EncryptionHelper
import com.example.passwordmanager.ui.CreateAccountScreen
import com.example.passwordmanager.ui.HomeScreen
import com.example.passwordmanager.ui.SettingsScreen
import com.example.passwordmanager.ui.theme.PasswordManagerTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PasswordManagerTheme {
                val navController = rememberNavController()
                val accounts = remember { mutableStateListOf<Account>() }

                // Load accounts when activity starts
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

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable("home") {
                            HomeScreen(navController = navController, accounts = accounts)
                        }
                        composable("createAccount") {
                            CreateAccountScreen(
                                navController = navController,
                                modifier = Modifier,
                                accounts = accounts
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                navController = navController,
                                onImport = { importedAccounts ->
                                    accounts.clear()
                                    accounts.addAll(importedAccounts)
                                },
                                onExport = { accounts.toList() },
                                onDeleteAll = { accounts.clear() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    PasswordManagerTheme {
        val navController = rememberNavController()
        val accounts = remember { mutableStateListOf<Account>() }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("home") {
                    HomeScreen(navController = navController, accounts = accounts)
                }
                composable("createAccount") {
                    CreateAccountScreen(
                        navController = navController,
                        modifier = Modifier,
                        accounts = accounts
                    )
                }
                composable("settings") {
                    SettingsScreen(
                        navController = navController,
                        onImport = { importedAccounts ->
                            accounts.clear()
                            accounts.addAll(importedAccounts)
                        },
                        onExport = { accounts.toList() },
                        onDeleteAll = { accounts.clear() }
                    )
                }
            }
        }
    }
}