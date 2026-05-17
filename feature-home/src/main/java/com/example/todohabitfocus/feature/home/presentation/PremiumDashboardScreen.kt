package com.example.todohabitfocus.feature.home.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.core.designsystem.theme.PastelBlue
import com.example.todohabitfocus.core.designsystem.theme.PastelGreen
import com.example.todohabitfocus.core.designsystem.theme.PastelPurple
import com.example.todohabitfocus.core.domain.model.Priority
import com.example.todohabitfocus.core.domain.model.Task
import com.example.todohabitfocus.core.ui.components.PriorityChip
import com.example.todohabitfocus.core.ui.components.ProgressRing
import com.example.todohabitfocus.feature.home.presentation.components.*

@Composable
fun PremiumDashboardRoute(
    onNavigateToTasks: () -> Unit = {},
    onNavigateToHabits: () -> Unit = {},
    onNavigateToFocus: () -> Unit = {}
) {
    PremiumDashboardScreen(
        onNavigateToTasks = onNavigateToTasks,
        onNavigateToHabits = onNavigateToHabits,
        onNavigateToFocus = onNavigateToFocus
    )
}

@Composable
fun PremiumDashboardScreen(
    onNavigateToTasks: () -> Unit,
    onNavigateToHabits: () -> Unit,
    onNavigateToFocus: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(800)) + slideInVertically(
                initialOffsetY = { 40 },
                animationSpec = tween(800)
            )
        ) {
            com.example.todohabitfocus.core.ui.components.PullToRefreshWrapper(
                isRefreshing = uiState.isLoading,
                onRefresh = { viewModel.loadDashboardData() } // Assuming this exists or similar
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item { 
                        DashboardHeader(uiState.userName)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    item {
                        StatsOverviewRow(
                            upcoming = uiState.upcomingTasks.size,
                            today = uiState.upcomingTasks.count { it.dueDate != null }, // Simplified logic for mock
                            completed = 12 // Mock completed count
                        )
                    }
                    
                    item {
                        SectionHeader("Focus Stats", onActionClick = onNavigateToFocus)
                        FocusStatsCard(
                            time = "${uiState.focusStats.totalFocusTimeMinutes}m",
                            sessions = uiState.focusStats.sessionsCompleted,
                            progress = 0.65f // Mock progress
                        )
                    }

                    item {
                        SectionHeader("Recent Tasks", onActionClick = onNavigateToTasks)
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            uiState.upcomingTasks.take(3).forEach { task ->
                                PremiumTaskCard(task)
                            }
                        }
                    }
                    
                    item {
                        SectionHeader("Habit Streaks", onActionClick = onNavigateToHabits)
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 8.dp)
                        ) {
                            items(uiState.habitStreaks) { streak ->
                                PremiumHabitStreakItem(streak)
                            }
                        }
                    }
                    
                    item {
                        SectionHeader("Weekly Activity")
                        WeeklyActivityChart(uiState.weeklyActivity)
                    }
                    
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
            }
        }
    }
}

@Composable
fun DashboardHeader(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hello,",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$userName ✨",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = userName.take(1).uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun StatsOverviewRow(upcoming: Int, today: Int, completed: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SmallStatCard("Upcoming", upcoming.toString(), PastelBlue, Modifier.weight(1f))
        SmallStatCard("Today", today.toString(), PastelPurple, Modifier.weight(1f))
        SmallStatCard("Done", completed.toString(), PastelGreen, Modifier.weight(1f))
    }
}

@Composable
fun SmallStatCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun PremiumTaskCard(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProgressRing(
                progress = if (task.isCompleted) 1f else 0.3f,
                modifier = Modifier.size(42.dp),
                strokeWidth = 4f
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Due Today",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            PriorityChip(priority = task.priority.name)
        }
    }
}

@Composable
fun FocusStatsCard(time: String, sessions: Int, progress: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF6366F1), Color(0xFFA855F7))
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Deep Focus",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        time,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        "$sessions sessions completed",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { progress },
                        color = Color.White,
                        strokeWidth = 8.dp,
                        modifier = Modifier.size(80.dp),
                        trackColor = Color.White.copy(alpha = 0.2f)
                    )
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumHabitStreakItem(streak: HabitStreak) {
    Card(
        modifier = Modifier.width(110.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (streak.isCompletedToday) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Whatshot,
                contentDescription = null,
                tint = if (streak.isCompletedToday) MaterialTheme.colorScheme.primary else Color(0xFFFFB74D),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${streak.streakCount}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = streak.habit.name,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}
