package com.example.passwordmanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.data.Account

@Composable
fun AccountItem(account: Account) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .clickable { /* Handle account click */ }
        ) {
            Text(
                text = account.getName(),
                color = Color.Blue,
                fontSize = 25.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
            Text(
                text = account.getPassword(),
                color = Color.Black,
                fontSize = 25.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            )
        }

        Divider(
            color = Color.Gray, // Google-style separator
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
