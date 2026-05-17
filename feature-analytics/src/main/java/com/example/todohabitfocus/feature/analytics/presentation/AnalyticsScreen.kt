package com.example.todohabitfocus.feature.analytics.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.feature.analytics.presentation.components.ConsistencyCard
import com.example.todohabitfocus.feature.analytics.presentation.components.LineChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onBackClick: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Performance Analytics") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                uiState.summary?.let { summary ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        item {
                            SummaryCards(summary.totalFocusHours, summary.tasksCompleted, summary.avgProductivityScore)
                        }
                        
                        item {
                            AnalyticsSection("Productivity Trend") {
                                LineChart(
                                    data = summary.weeklyTrends,
                                    modifier = Modifier.fillMaxWidth().height(180.dp)
                                )
                            }
                        }

                        item {
                            AnalyticsSection("Habit Consistency") {
                                summary.habitConsistencies.forEach { habit ->
                                    ConsistencyCard(habit.habitName, habit.percentage, Color(habit.color))
                                }
                            }
                        }

                        item {
                            AnalyticsSection("Focus Hours") {
                                LineChart(
                                    data = summary.focusHourTrends,
                                    modifier = Modifier.fillMaxWidth().height(180.dp),
                                    lineColor = Color(0xFFE91E63)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryCards(focusHours: Float, tasks: Int, score: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryItem("Focus", "${focusHours}h", Modifier.weight(1f), Color(0xFF673AB7))
        SummaryItem("Tasks", "$tasks", Modifier.weight(1f), Color(0xFF4CAF50))
        SummaryItem("Score", "$score%", Modifier.weight(1f), Color(0xFFFF9800))
    }
}

@Composable
fun SummaryItem(label: String, value: String, modifier: Modifier, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = color)
            Text(label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun AnalyticsSection(title: String, content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(800)) + slideInVertically(initialOffsetY = { 40 })
    ) {
        Column {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}
