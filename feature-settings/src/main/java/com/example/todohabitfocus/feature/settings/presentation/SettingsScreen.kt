package com.example.todohabitfocus.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todohabitfocus.feature.settings.presentation.components.SettingsGroup
import com.example.todohabitfocus.feature.settings.presentation.components.SettingsItem
import com.example.todohabitfocus.feature.settings.presentation.components.SettingsSwitchItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var darkMode by remember { mutableStateOf(false) }
    var remindersEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.ExtraBold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Overview
            ProfileCard(
                name = "Alex Johnson",
                email = "alex.j@example.com",
                onClick = onNavigateToProfile
            )

            // Appearance
            SettingsGroup(title = "Appearance") {
                SettingsSwitchItem(
                    icon = Icons.Default.DarkMode,
                    iconColor = Color(0xFF6366F1),
                    title = "Dark Mode",
                    checked = darkMode,
                    onCheckedChange = { darkMode = it }
                )
            }

            // General Settings
            SettingsGroup(title = "General") {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    iconColor = Color(0xFFF59E0B),
                    title = "Notifications",
                    subtitle = "Alerts, sounds, and badges",
                    onClick = onNavigateToNotifications
                )
                SettingsSwitchItem(
                    icon = Icons.Default.AccessTime,
                    iconColor = Color(0xFF10B981),
                    title = "Daily Reminders",
                    checked = remindersEnabled,
                    onCheckedChange = { remindersEnabled = it }
                )
                SettingsItem(
                    icon = Icons.Default.Language,
                    iconColor = Color(0xFF3B82F6),
                    title = "Language",
                    subtitle = "English (US)",
                    onClick = { /* Select Language */ }
                )
            }

            // Privacy & Security
            SettingsGroup(title = "Privacy & Security") {
                SettingsItem(
                    icon = Icons.Default.Lock,
                    iconColor = Color(0xFFEF4444),
                    title = "Privacy Policy",
                    onClick = { /* Open Privacy */ }
                )
                SettingsItem(
                    icon = Icons.Default.Security,
                    iconColor = Color(0xFF6366F1),
                    title = "Account Security",
                    onClick = { /* Account Security */ }
                )
            }

            // Account Actions
            SettingsGroup(title = "Account") {
                SettingsItem(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    iconColor = Color(0xFF64748B),
                    title = "Sign Out",
                    onClick = { /* Sign Out */ },
                    trailing = {} // No chevron
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ProfileCard(name: String, email: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.take(1).uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
