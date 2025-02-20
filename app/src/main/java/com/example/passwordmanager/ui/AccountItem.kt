package com.example.passwordmanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.data.Account

@Composable
fun AccountItem(account: Account) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val passwordColor = if (isSystemInDarkTheme()) {
        Color(0xFF81D4FA)
    } else {
        Color(0xFF1565C0)
    }
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clickable { isPasswordVisible = !isPasswordVisible } // Toggle password visibility
        ) {
            Text(
                text = account.getName(),
                fontSize = 25.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            Text(
                text = if (isPasswordVisible) account.getPassword() else "••••••••",
                fontSize = 35.sp,
                color = passwordColor,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .align(Alignment.BottomStart)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
        )
    }
}
