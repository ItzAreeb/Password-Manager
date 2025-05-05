package com.example.passwordmanager.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.passwordmanager.data.Account
import com.example.passwordmanager.ui.security.SecurityManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    securityManager: SecurityManager,
    onSecurityChanged: (Boolean) -> Unit,
    onImport: (List<Account>) -> Unit,
    onExport: () -> List<Account>,
    onDeleteAll: () -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showSecurityDialog by remember { mutableStateOf(false) }

    // File picker launchers
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                try {
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        val jsonString = reader.readText()
                        val type = object : TypeToken<List<Account>>() {}.type
                        val importedAccounts = Gson().fromJson<List<Account>>(jsonString, type)
                        onImport(importedAccounts ?: emptyList())
                    }
                } catch (e: Exception) {
                    errorMessage = "Failed to import: ${e.message}"
                }
            }
        }
    }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                try {
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        val jsonString = Gson().toJson(onExport())
                        outputStream.write(jsonString.toByteArray())
                        successMessage = "Accounts exported successfully"
                    }
                } catch (e: Exception) {
                    errorMessage = "Failed to export: ${e.message}"
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        TopAppBar(
            title = { Text("Settings", fontSize = 20.sp) },
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Security Section
            SettingsOption(
                icon = if (securityManager.isSecurityEnabled()) {
                    Icons.Default.Lock
                } else {
                    Icons.Default.LockOpen
                },
                text = if (securityManager.isSecurityEnabled()) {
                    "App Lock: Enabled"
                } else {
                    "App Lock: Disabled"
                },
                onClick = {
                    keyboardController?.hide()
                    showSecurityDialog = true
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Import Section
            SettingsOption(
                icon = Icons.Default.Upload,
                text = "Import Accounts",
                onClick = {
                    keyboardController?.hide()
                    errorMessage = null
                    successMessage = null
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "application/json"
                    }
                    importLauncher.launch(intent)
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Export Section
            SettingsOption(
                icon = Icons.Default.Download,
                text = "Export Accounts",
                onClick = {
                    keyboardController?.hide()
                    errorMessage = null
                    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "application/json"
                        putExtra(Intent.EXTRA_TITLE, "accounts_export.json")
                    }
                    exportLauncher.launch(intent)
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Delete All Section
            SettingsOption(
                icon = Icons.Default.Delete,
                text = "Delete All Accounts",
                color = MaterialTheme.colorScheme.error,
                onClick = {
                    keyboardController?.hide()
                    showDeleteDialog = true
                }
            )

            // Status messages
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (successMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = successMessage!!,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (showDeleteDialog) {
                DeleteAllConfirmationDialog(
                    onDismiss = { showDeleteDialog = false },
                    onConfirm = {
                        onDeleteAll()
                        showDeleteDialog = false
                    }
                )
            }
            if (showSecurityDialog) {
                SecuritySettingsDialog(
                    securityManager = securityManager,
                    onDismiss = { showSecurityDialog = false },
                    onSecurityChanged = { enabled ->
                        onSecurityChanged(enabled)
                        showSecurityDialog = false
                    },
                    context = context
                )
            }
        }
    }
}

@Composable
private fun DeleteAllConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete All Accounts",
                color = MaterialTheme.colorScheme.error
            )
        },
        text = {
            Text("Are you sure you want to delete ALL accounts? This action cannot be undone.")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Delete All")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun SettingsOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = color
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun SecuritySettingsDialog(
    securityManager: SecurityManager,
    onDismiss: () -> Unit,
    onSecurityChanged: (Boolean) -> Unit,
    context: android.content.Context
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("App Security") },
        text = {
            Column {
                if (securityManager.isSecurityEnabled()) {
                    Text("Do you want to disable app lock?")
                } else {
                    Text("Enable security to protect your passwords with:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Fingerprint")
                    Text("• PIN")
                    Text("• Pattern")
                    Text("• Password")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (securityManager.isSecurityEnabled()) {
                        securityManager.setSecurityEnabled(false)
                        onSecurityChanged(false)
                    } else {
                        (context as? FragmentActivity)?.let { activity ->
                            securityManager.setupSecurity(activity) {
                                onSecurityChanged(true)
                            }
                        }
                    }
                    onDismiss()
                }
            ) {
                Text(if (securityManager.isSecurityEnabled()) "Disable" else "Enable")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}