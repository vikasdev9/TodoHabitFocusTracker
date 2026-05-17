package com.example.todohabitfocus.core.notification.permission

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NotificationPermissionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Notification Permission",
    description: String = "This app needs notification permission to remind you about your tasks and habits. Please grant the permission to stay on track.",
    confirmText: String = "Grant Permission",
    dismissText: String = "Not Now",
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            Text(text = description)
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissText)
            }
        },
        modifier = modifier
    )
}

@Composable
fun SettingsPermissionDialog(
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    NotificationPermissionDialog(
        onDismiss = onDismiss,
        onConfirm = onOpenSettings,
        modifier = modifier,
        title = "Permission Denied",
        description = "Notification permission is permanently denied. You can enable it in the app settings to receive reminders.",
        confirmText = "Open Settings",
        dismissText = "Cancel"
    )
}
