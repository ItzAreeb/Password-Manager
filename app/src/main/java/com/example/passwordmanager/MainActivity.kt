package com.example.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.passwordmanager.data.Account
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
                        composable("home") { HomePage(navController, Modifier.padding(innerPadding), accounts) }
                        composable("createAccount") { CreateAccount(navController, Modifier.padding(innerPadding), accounts) }
                    }
                }
            }
        }
    }
}


@Composable
fun HomePage(navController: NavController, modifier: Modifier = Modifier, accounts: List<Account>) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Password Manager",
            color = Color.Black,
            fontSize = 30.sp,
            modifier = Modifier.padding(16.dp)
        )

        for (i in accounts.indices) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .clickable { /* Handle account click */ }
                ) {
                    Text(
                        text = accounts[i].getName(),
                        color = Color.Blue,
                        fontSize = 25.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                    )
                    Text(
                        text = accounts[i].getPassword(),
                        color = Color.Black,
                        fontSize = 25.sp,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                    )
                }

                Divider(
                    color = Color.Gray, // Use a subtle gray color
                    thickness = 1.dp, // Thin line for separation
                    modifier = Modifier.padding(horizontal = 16.dp) // Adds margin to match text alignment
                )
            }
        }


        Spacer(modifier = Modifier.weight(1f)) // Pushes button to bottom

        Button(
            onClick = { navController.navigate("createAccount") },
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.End)
                .size(70.dp), // Increase button size
            shape = RoundedCornerShape(17.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C4043))
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Account",
                tint = Color.White,
                modifier = Modifier.size(50.dp) // Explicitly set icon size
            )
        }


    }
}


@Composable
fun CreateAccount(navController: NavController, modifier: Modifier = Modifier, accounts: MutableList<Account>) {
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
            onClick = {
                val newAccount = Account()
                newAccount.setName(username.text)
                newAccount.setPassword(password.text)
                if (newAccount.getName().isNotEmpty() && newAccount.getPassword().isNotEmpty()) accounts.add(newAccount)
                navController.popBackStack()
                      },
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
        val accounts = remember { mutableStateListOf<Account>() }
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(navController, startDestination = "home") {
                composable("home") {
                    HomePage(navController, Modifier.padding(innerPadding), accounts)
                }
                composable("createAccount") { CreateAccount(navController, Modifier.padding(innerPadding), accounts) }
            }
        }
    }
}
