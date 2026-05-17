package com.example.todohabitfocus.core.notification.permission

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun NotificationPermissionHandler(
    permissionManager: NotificationPermissionManager,
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: () -> Unit = {},
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        LaunchedEffect(Unit) {
            onPermissionGranted()
        }
        return
    }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = context as? Activity
    var showRationale by remember { mutableStateOf(value = false) }
    var showSettingsDialog by remember { mutableStateOf(value = false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionManager.setPermissionRequested(true)
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    fun checkPermission() {
        if (permissionManager.hasNotificationPermission()) {
            onPermissionGranted()
            showRationale = false
            showSettingsDialog = false
        } else {
            if (activity != null) {
                when {
                    permissionManager.shouldShowPermissionRationale(activity) -> {
                        showRationale = true
                    }
                    !permissionManager.isPermissionRequested() -> {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    else -> {
                        showSettingsDialog = true
                    }
                }
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                checkPermission()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (showRationale) {
        NotificationPermissionDialog(
            onDismiss = {
                showRationale = false
                permissionManager.setPermissionRequested(true)
                onPermissionDenied()
            },
            onConfirm = {
                showRationale = false
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        )
    }

    if (showSettingsDialog) {
        SettingsPermissionDialog(
            onDismiss = {
                showSettingsDialog = false
                onPermissionDenied()
            },
            onOpenSettings = {
                showSettingsDialog = false
                permissionManager.openAppSettings()
            }
        )
    }
}
