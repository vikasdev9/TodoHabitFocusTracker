package com.example.todohabitfocus.core.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todohabitfocus.core.designsystem.theme.AppTheme

/**
 * A custom Card component with premium styling.
 * Used across the app for Tasks, Habits, and Analytics.
 */
@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = AppTheme.elevation.small
        )
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}
