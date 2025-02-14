package com.example.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.passwordmanager.ui.theme.PasswordManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PasswordManagerTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController, startDestination = "home") {
                        composable("home") { HomePage(navController, Modifier.padding(innerPadding)) }
                        composable("createAccount") { CreateAccount(navController) }
                    }
                }
            }
        }
    }
}


@Composable
fun HomePage(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Password Manager",
            color = Color.Blue,
            fontSize = 30.sp,
            modifier = Modifier.padding(16.dp)
        )

        for (i in 1..6) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .padding(8.dp)
            ) {
                Text(text = "sample@email.com", color = Color.Blue, fontSize = 25.sp)
                Text(
                    text = "password",
                    color = Color.Blue,
                    fontSize = 25.sp,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Pushes button to bottom

        Button(
            onClick = { navController.navigate("createAccount") }, // Navigates to Create Account page
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Create Account")
        }
    }
}


@Composable
fun CreateAccount(navController: NavController, modifier: Modifier = Modifier) {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            fontSize = 30.sp,
            color = Color.Blue,
            modifier = Modifier.padding(16.dp)
        )

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Handle account creation logic here */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() }, // Navigate back to Home Page
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PasswordManagerTheme {
        val navController = rememberNavController()

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(navController, startDestination = "home") {
                composable("home") { HomePage(navController, Modifier.padding(innerPadding)) }
                composable("createAccount") { CreateAccount(navController) }
            }
        }
    }
}
