package com.example.passwordmanager.ui

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.data.Account
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccountItem(
    account: Account,
    context: Context,
    onEdit: (Account) -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var isSwiped by remember { mutableStateOf(false) }
    val swipeThreshold = 100.dp.value * context.resources.displayMetrics.density

    val passwordColor = if (isSystemInDarkTheme()) {
        Color(0xFF81D4FA)
    } else {
        Color(0xFF1565C0)
    }

    // ClipboardManager instance
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    Column {
        Layout(
            content = {
                if (isSwiped) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(passwordColor)
                    ) {
                        Text(
                            text = "Edit",
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 16.dp),
                            color = Color.White
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .offset { IntOffset(offsetX.roundToInt(), 0) }
                        .background(MaterialTheme.colorScheme.background)
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    if (offsetX > swipeThreshold) {
                                        onEdit(account)
                                    }
                                    offsetX = 0f
                                    isSwiped = false
                                },
                                onHorizontalDrag = { _, dragAmount ->
                                    // Only allow swiping to the right (positive dragAmount)
                                    if (dragAmount.toInt() != 0 || isSwiped) {
                                        offsetX = (offsetX + dragAmount).coerceAtLeast(0f)
                                        isSwiped = offsetX > 0
                                    }
                                }
                            )
                        }
                        .combinedClickable(
                            onClick = { isPasswordVisible = !isPasswordVisible },
                            onLongClick = {
                                if (isPasswordVisible) {
                                    val clip = android.content.ClipData.newPlainText(
                                        "Password",
                                        account.getPassword()
                                    )
                                    clipboardManager.setPrimaryClip(clip)
                                    Toast.makeText(
                                        context,
                                        "Password copied to clipboard",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                ) {
                    // Background that becomes visible when swiped
                    if (isSwiped) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray.copy(alpha = 0.2f))
                        )
                    }

                    // Foreground content
                    Box(modifier = Modifier.fillMaxSize()) {
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
                }
            }
        ) { measurable, constraints ->
            // Measure both boxes
            val placeable = measurable.map { it.measure(constraints) }

            // Position them at (0, 0) relative to the Layout
            layout(constraints.maxWidth, 80.dp.roundToPx()) {
                placeable.forEach { it.place(0, 0) }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
        )
    }
}