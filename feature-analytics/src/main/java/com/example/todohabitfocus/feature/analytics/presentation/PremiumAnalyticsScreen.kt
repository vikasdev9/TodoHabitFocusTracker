package com.example.todohabitfocus.feature.analytics.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.todohabitfocus.feature.analytics.presentation.components.ConsistencyCard
import com.example.todohabitfocus.feature.analytics.presentation.components.LineChart
import com.example.todohabitfocus.feature.analytics.presentation.components.PremiumBarChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumAnalyticsScreen(
    onBackClick: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedReportType by remember { mutableStateOf("Weekly") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Performance Insights", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                uiState.summary?.let { summary ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        item {
                            ReportTypeSelector(
                                selected = selectedReportType,
                                onSelect = { selectedReportType = it }
                            )
                        }

                        item {
                            PremiumProductivityScoreCard(score = summary.avgProductivityScore)
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                PremiumStatCard(
                                    label = "Focus Time",
                                    value = "${summary.totalFocusHours}h",
                                    icon = Icons.Default.Timer,
                                    color = Color(0xFF6366F1),
                                    modifier = Modifier.weight(1f)
                                )
                                PremiumStatCard(
                                    label = "Tasks Done",
                                    value = "${summary.tasksCompleted}",
                                    icon = Icons.Default.CheckCircle,
                                    color = Color(0xFF10B981),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        item {
                            AnalyticsCardWrapper(title = "Productivity Trend") {
                                LineChart(
                                    data = summary.weeklyTrends,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    lineColor = Color(0xFF6366F1)
                                )
                            }
                        }

                        item {
                            AnalyticsCardWrapper(title = "Daily Focus Distribution") {
                                PremiumBarChart(
                                    data = summary.focusHourTrends,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )
                            }
                        }

                        item {
                            AnalyticsCardWrapper(title = "Habit Consistency") {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    summary.habitConsistencies.forEach { habit ->
                                        ConsistencyCard(
                                            name = habit.habitName,
                                            percentage = habit.percentage,
                                            color = Color(habit.color)
                                        )
                                    }
                                }
                            }
                        }
                        
                        item { Spacer(modifier = Modifier.height(32.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun ReportTypeSelector(selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        listOf("Weekly", "Monthly", "Yearly").forEach { type ->
            val isSelected = selected == type
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable { onSelect(type) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = type,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun PremiumProductivityScoreCard(score: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF4F46E5), Color(0xFF7C3AED))
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
                        "Productivity Score",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        "$score%",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        "12% higher than last week",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.15f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { score / 100f },
                            modifier = Modifier.size(60.dp),
                            color = Color.White,
                            strokeWidth = 6.dp,
                            trackColor = Color.White.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumStatCard(
    label: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun AnalyticsCardWrapper(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            content()
        }
    }
}
