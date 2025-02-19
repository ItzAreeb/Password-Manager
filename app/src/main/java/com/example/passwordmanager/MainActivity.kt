package com.example.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.*
import com.example.passwordmanager.data.Account
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

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController, startDestination = "home") {
                        composable("home") { HomeScreen(navController, Modifier.padding(innerPadding), accounts) }
                        composable("createAccount") { CreateAccountScreen(navController, Modifier.padding(innerPadding), accounts) }
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
                composable("home") { HomeScreen(navController, Modifier.padding(innerPadding), accounts) }
                composable("createAccount") { CreateAccountScreen(navController, Modifier.padding(innerPadding), accounts) }
            }
        }
    }
}
