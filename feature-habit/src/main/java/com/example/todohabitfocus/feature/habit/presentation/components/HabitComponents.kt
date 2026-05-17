package com.example.todohabitfocus.feature.habit.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todohabitfocus.core.designsystem.theme.PastelGreen
import com.example.todohabitfocus.feature.habit.presentation.HabitItemState
import java.util.*

@Composable
fun PremiumHabitCard(
    habitState: HabitItemState,
    onToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val habit = habitState.habit
    val isCompleted = habitState.isCompletedToday
    
    val backgroundColor = if (isCompleted) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
    } else {
        getHabitColor(habit.name).copy(alpha = 0.15f)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rounded Square Icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getHabitIcon(habit.name),
                    fontSize = 28.sp,
                    modifier = Modifier.animateContentSize()
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (habitState.currentStreak > 0) Color(0xFFFF9800) else MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${habitState.currentStreak} day streak",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Weekly Mini Progress
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(7) { i ->
                        Box(
                            modifier = Modifier
                                .size(width = 20.dp, height = 4.dp)
                                .clip(CircleShape)
                                .background(
                                    if (i < 3) getHabitColor(habit.name) // Mock completed days
                                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                                )
                        )
                    }
                }
            }

            // Completion Toggle with Bounce
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isCompleted) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .border(2.dp, if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), CircleShape)
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

private fun getHabitColor(name: String): Color {
    return when {
        name.contains("water", ignoreCase = true) -> Color(0xFF64B5F6)
        name.contains("read", ignoreCase = true) -> Color(0xFF81C784)
        name.contains("exercise", ignoreCase = true) || name.contains("gym", ignoreCase = true) -> Color(0xFFFF8A65)
        name.contains("meditate", ignoreCase = true) -> Color(0xFF9575CD)
        name.contains("code", ignoreCase = true) || name.contains("study", ignoreCase = true) -> Color(0xFFFFD54F)
        else -> Color(0xFF4DB6AC)
    }
}


@Composable
fun CalendarHeatmap(
    logs: List<Long>,
    modifier: Modifier = Modifier
) {
    val color = MaterialTheme.colorScheme.primary
    val emptyColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
    
    Column(modifier = modifier) {
        Text(
            "Activity Heatmap",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -20)
            
            repeat(21) {
                val date = calendar.timeInMillis
                val isCompleted = logs.any { isSameDay(it, date) }
                
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (isCompleted) color else emptyColor)
                )
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
        }
    }
}

@Composable
fun StreakPulse(streak: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "streak")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                Icons.Default.LocalFireDepartment,
                contentDescription = null,
                modifier = Modifier.size(80.dp).scale(scale),
                tint = Color(0xFFFF5722)
            )
            Text(
                text = streak.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Text(
            "Day Streak!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF5722)
        )
    }
}

private fun getHabitIcon(name: String): String {
    return when {
        name.contains("water", ignoreCase = true) -> "💧"
        name.contains("read", ignoreCase = true) -> "📚"
        name.contains("exercise", ignoreCase = true) -> "💪"
        name.contains("meditate", ignoreCase = true) -> "🧘"
        name.contains("code", ignoreCase = true) -> "💻"
        else -> "✨"
    }
}

private fun isSameDay(d1: Long, d2: Long): Boolean {
    val c1 = Calendar.getInstance().apply { timeInMillis = d1 }
    val c2 = Calendar.getInstance().apply { timeInMillis = d2 }
    return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
           c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
}
