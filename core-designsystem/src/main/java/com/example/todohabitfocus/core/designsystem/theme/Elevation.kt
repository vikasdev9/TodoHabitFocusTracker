package com.example.todohabitfocus.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Minimal Elevation System
 * Focused on soft depth without heavy shadows.
 */

@Immutable
data class AppElevation(
    val default: Dp = 0.dp,
    val extraSmall: Dp = 1.dp,
    val small: Dp = 2.dp,
    val medium: Dp = 4.dp,
    val large: Dp = 8.dp
)

val LocalElevation = staticCompositionLocalOf { AppElevation() }
