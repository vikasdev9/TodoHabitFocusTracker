package com.example.todohabitfocus.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Immutable
data class Gradients(
    val primary: Brush = Brush.linearGradient(
        colors = listOf(LightPrimary, LightPrimaryContainer)
    ),
    val secondary: Brush = Brush.linearGradient(
        colors = listOf(LightSecondary, LightSecondaryContainer)
    ),
    val tertiary: Brush = Brush.linearGradient(
        colors = listOf(LightTertiary, LightTertiaryContainer)
    ),
    val surface: Brush = Brush.verticalGradient(
        colors = listOf(Color.White.copy(alpha = 0.1f), Color.Transparent)
    )
)

val LocalGradients = staticCompositionLocalOf { Gradients() }
