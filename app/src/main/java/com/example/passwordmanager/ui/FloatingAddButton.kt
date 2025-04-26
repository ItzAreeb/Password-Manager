package com.example.passwordmanager.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun FloatingAddButton(
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    FloatingActionButton(
        onClick = { if (enabled) onClick() },  // Only call onClick when enabled
        modifier = Modifier
            .size(58.dp)
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.6f),  // Visual feedback for disabled state
        shape = RoundedCornerShape(17.dp),
        containerColor = if (enabled) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        },
        contentColor = if (enabled) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Create Account",
            modifier = Modifier.size(40.dp)
        )
    }
}
