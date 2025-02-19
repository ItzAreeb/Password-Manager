package com.example.passwordmanager.ui

import androidx.compose.foundation.clickable
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

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .clickable { isPasswordVisible = !isPasswordVisible } // Toggle password visibility
        ) {
            Text(
                text = account.getName(),
                color = Color.Black,
                fontSize = 25.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
            Text(
                text = if (isPasswordVisible) account.getPassword() else "••••••••",
                color = Color(0xFF3C4043),
                fontSize = 25.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = Color.Gray // Google-style separator
        )
    }
}
