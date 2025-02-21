package com.example.passwordmanager.ui

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccountItem(account: Account, context: Context) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val passwordColor = if (isSystemInDarkTheme()) {
        Color(0xFF81D4FA)
    } else {
        Color(0xFF1565C0)
    }

    // ClipboardManager instance
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .combinedClickable(
                    onClick = { isPasswordVisible = !isPasswordVisible },
                    onLongClick = {
                        val clip = android.content.ClipData.newPlainText(
                            "Password",
                            account.getPassword()
                        )
                        clipboardManager.setPrimaryClip(clip)
                        Toast.makeText(context, "Password copied to clipboard", Toast.LENGTH_SHORT).show()
                    }
                )
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
