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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.feature.home.presentation.components.*

@Composable
fun HomeRoute(
    onNavigateToTasks: () -> Unit = {},
    onNavigateToHabits: () -> Unit = {},
    onNavigateToFocus: () -> Unit = {}
) {
    HomeScreen(
        onNavigateToTasks = onNavigateToTasks,
        onNavigateToHabits = onNavigateToHabits,
        onNavigateToFocus = onNavigateToFocus
    )
}

@Composable
fun HomeScreen(
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
            .background(Color(0xFFF8F9FA))
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(initialOffsetY = { 40 })
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { GreetingSection(uiState.userName) }
                
                item { ProductivityScoreCard(uiState.productivityScore) }
                
                item {
                    SectionHeader("Upcoming Tasks", onActionClick = onNavigateToTasks)
                    uiState.upcomingTasks.forEach { task ->
                        UpcomingTaskCard(task)
                    }
                }
                
                item {
                    SectionHeader("Habit Streaks", onActionClick = onNavigateToHabits)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        items(uiState.habitStreaks) { streak ->
                            HabitStreakItem(streak)
                        }
                    }
                }
                
                item {
                    SectionHeader("Focus Stats", onActionClick = onNavigateToFocus)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatCard(
                            title = "Total Focus",
                            value = "${uiState.focusStats.totalFocusTimeMinutes}m",
                            icon = Icons.Default.Timer,
                            color = Color(0xFF3B82F6),
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Sessions",
                            value = "${uiState.focusStats.sessionsCompleted}",
                            icon = Icons.Default.TaskAlt,
                            color = Color(0xFF10B981),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                item {
                    WeeklyActivityChart(uiState.weeklyActivity)
                }
                
                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}
