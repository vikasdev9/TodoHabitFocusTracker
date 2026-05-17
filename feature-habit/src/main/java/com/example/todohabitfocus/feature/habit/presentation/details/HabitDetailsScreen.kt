package com.example.todohabitfocus.feature.habit.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.feature.habit.presentation.HabitViewModel
import com.example.todohabitfocus.feature.habit.presentation.components.CalendarHeatmap
import com.example.todohabitfocus.feature.habit.presentation.components.StreakPulse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailsScreen(
    habitId: String,
    onNavigateBack: () -> Unit,
    viewModel: HabitViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val habitState = uiState.habits.find { it.habit.id == habitId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.background(Color.White, CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* Delete */ },
                        modifier = Modifier.background(Color.White, CircleShape)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        habitState?.let { state ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Header Icon
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(getEmojiForHabit(state.habit.name), fontSize = 48.sp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = state.habit.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Build consistency every day",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                StreakPulse(streak = state.currentStreak)

                // Stats Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        CalendarHeatmap(logs = state.logs.map { it.date })
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DetailStatItem(
                                label = "Best Streak",
                                value = "${state.currentStreak}d",
                                icon = Icons.Default.EmojiEvents,
                                color = Color(0xFFFFD54F)
                            )
                            DetailStatItem(
                                label = "Completion",
                                value = "92%",
                                icon = Icons.Default.Timeline,
                                color = Color(0xFF81C784)
                            )
                            DetailStatItem(
                                label = "Total Days",
                                value = "${state.logs.size}",
                                icon = Icons.Default.CalendarToday,
                                color = Color(0xFF64B5F6)
                            )
                        }
                    }
                }

                // Weekly Target Section (Mock)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Weekly Target",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val days = listOf("M", "T", "W", "T", "F", "S", "S")
                        days.forEachIndexed { i, day ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (i < 4) MaterialTheme.colorScheme.primary 
                                            else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (i < 4) Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(day, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun DetailStatItem(label: String, value: String, icon: ImageVector, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(44.dp).background(color.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun getEmojiForHabit(name: String): String {
    return when {
        name.contains("water", ignoreCase = true) -> "💧"
        name.contains("read", ignoreCase = true) -> "📚"
        name.contains("exercise", ignoreCase = true) -> "💪"
        name.contains("meditate", ignoreCase = true) -> "🧘"
        name.contains("code", ignoreCase = true) -> "💻"
        name.contains("run", ignoreCase = true) -> "🏃"
        else -> "✨"
    }
}

