package com.example.passwordmanager.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.passwordmanager.data.Account

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    accounts: MutableList<Account>,
    existingAccount: Account? = null
) {
    var username by remember { mutableStateOf(TextFieldValue(existingAccount?.getName() ?: "")) }
    var password by remember { mutableStateOf(TextFieldValue(existingAccount?.getPassword() ?: "")) }
    var passwordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val configuration = LocalConfiguration.current
    configuration.screenHeightDp.dp
    val imePadding = WindowInsets.ime.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        TopAppBar(
            title = {
                Text(
                    if (existingAccount != null) "Edit Account" else "Add Account Details",
                    fontSize = 20.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .weight(1f, fill = false)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password generator button
            Button(
                onClick = {
                    password = TextFieldValue(generateStrongPassword())
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generate Strong Password")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 12.dp + imePadding
                )
        ) {
            Button(
                onClick = {
                    keyboardController?.hide()
                    if (existingAccount != null) {
                        existingAccount.setName(username.text)
                        existingAccount.setPassword(password.text)
                    } else {
                        val newAccount = Account().apply {
                            setName(username.text)
                            setPassword(password.text)
                        }
                        if (newAccount.getName().isNotEmpty() && newAccount.getPassword().isNotEmpty()) {
                            accounts.add(newAccount)
                        }
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (existingAccount != null) "Save" else "Add")
            }
        }
    }
}

// Password generator function
private fun generateStrongPassword(): String {
    val uppercase = ('A'..'Z').toList()
    val lowercase = ('a'..'z').toList()
    val digits = ('0'..'9').toList()
    val specials = listOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')')

    uppercase + lowercase + digits + specials
    val passwordLength = 16

    return (1..passwordLength).map {
        when (it % 4) {
            0 -> uppercase.random()
            1 -> lowercase.random()
            2 -> digits.random()
            else -> specials.random()
        }
    }.shuffled().joinToString("")
}
