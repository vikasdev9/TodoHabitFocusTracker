package com.example.todohabitfocus.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem("home", Icons.Rounded.Home, "Home")
    data object Tasks : BottomNavItem("tasks", Icons.Rounded.TaskAlt, "Tasks")
    data object Habits : BottomNavItem("habits", Icons.Rounded.Autorenew, "Habits")
    data object Focus : BottomNavItem("focus", Icons.Rounded.Timer, "Focus")
    data object Analytics : BottomNavItem("analytics", Icons.Rounded.BarChart, "Analytics")
}
