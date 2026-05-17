package com.example.todohabitfocus.feature.habit.presentation.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.feature.habit.presentation.HabitViewModel
import com.example.todohabitfocus.feature.habit.presentation.components.PremiumHabitCard

@Composable
fun HabitDashboardScreen(
    onAddHabitClick: () -> Unit,
    onHabitClick: (String) -> Unit,
    viewModel: HabitViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddHabitClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(600)) + slideInVertically(initialOffsetY = { 50 })
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    HabitDashboardHeader()
                }

                item {
                    WeeklyProgressSection()
                }

                item {
                    Text(
                        "Today's Habits",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (uiState.isLoading && uiState.habits.isEmpty()) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            repeat(3) {
                                com.example.todohabitfocus.core.ui.components.ShimmerEffect(
                                    modifier = Modifier.fillMaxWidth().height(100.dp),
                                    shape = RoundedCornerShape(24.dp)
                                )
                            }
                        }
                    }
                } else if (!uiState.isLoading && uiState.habits.isEmpty()) {
                    item {
                        EmptyHabitsState(modifier = Modifier.padding(top = 40.dp))
                    }
                } else {
                    item {
                        com.example.todohabitfocus.core.ui.components.PullToRefreshWrapper(
                            isRefreshing = uiState.isLoading,
                            onRefresh = { viewModel.loadHabits() } // Assuming loadHabits is public or needed
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                uiState.habits.forEach { habitState ->
                                    PremiumHabitCard(
                                        habitState = habitState,
                                        onToggle = { viewModel.toggleHabit(habitState.habit.id) },
                                        onClick = { onHabitClick(habitState.habit.id) }
                                    )
                                }
                            }
                        }
                    }
                }
                
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun HabitDashboardHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "Keep Growing,",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Your Journey 🌿",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        IconButton(
            onClick = { /* Navigate to Analytics */ },
            modifier = Modifier.background(Color.White, CircleShape)
        ) {
            Icon(Icons.Default.BarChart, contentDescription = "Analytics", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun WeeklyProgressSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Weekly Goal",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "85% Completed",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { 0.85f },
                        modifier = Modifier.size(56.dp),
                        strokeWidth = 6.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun EmptyHabitsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(100.dp).background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🌱", fontSize = 48.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("No habits tracked yet", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Start your first small habit today!", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
