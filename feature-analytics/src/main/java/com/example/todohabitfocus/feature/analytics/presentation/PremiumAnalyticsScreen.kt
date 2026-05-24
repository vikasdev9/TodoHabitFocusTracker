package com.example.todohabitfocus.feature.analytics.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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

@Immutable
data class AnalyticsStat(
    val title: String,
    val value: String,
    val subtitle: String,
    val icon: ImageVector,
    val background: Color,
    val accent: Color
)

@Composable
fun PremiumAnalyticsScreen(
    onBackClick: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {

    val stats = remember {
        listOf(
            AnalyticsStat(
                title = "Day streak",
                value = "28",
                subtitle = "Day streak",
                icon = Icons.Default.LocalFireDepartment,
                background = Color(0xFFF9DDCF),
                accent = Color(0xFFD96C3D)
            ),
            AnalyticsStat(
                title = "Tasks done",
                value = "146",
                subtitle = "Tasks done",
                icon = Icons.Default.Verified,
                background = Color(0xFFD8F3E4),
                accent = Color(0xFF1F7A5C)
            ),
            AnalyticsStat(
                title = "This month",
                value = "92h",
                subtitle = "This month",
                icon = Icons.Default.Schedule,
                background = Color(0xFFD9EEF9),
                accent = Color(0xFF2979A8)
            ),
            AnalyticsStat(
                title = "Habits",
                value = "6/6",
                subtitle = "Habits",
                icon = Icons.Default.TrendingUp,
                background = Color(0xFFE8DDF8),
                accent = Color(0xFF6C4AB6)
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7FB))
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // HEADER
        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF081225)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {

                    Text(
                        text = "Analytics",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Color(0xFF081225)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Your productivity at a glance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF7B8190)
                    )
                }
            }
        }

        // PRODUCTIVITY CARD
        item {
            ProductivityCard()
        }

        // FOCUS HOURS
        item {
            FocusHoursCard()
        }

        // STATS GRID
        item {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(250.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                userScrollEnabled = false
            ) {

                items(
                    items = stats,
                    key = { it.title }
                ) { stat ->

                    AnalyticsMiniCard(stat)
                }
            }
        }

        // HEATMAP
        item {
            HabitConsistencyCard()
        }
    }
}

@Composable
fun ProductivityCard() {

    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3554D1)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                Text(
                    text = "PRODUCTIVITY SCORE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.7f),
                    letterSpacing = 0.8.sp
                )

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "+12%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                verticalAlignment = Alignment.Bottom
            ) {

                Text(
                    text = "87",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color.White
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "/100",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            LinearProgressIndicator(
                progress = { 0.87f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
fun FocusHoursCard() {

    val days = remember {
        listOf("M", "T", "W", "T", "F", "S", "S")
    }

    val heights = remember {
        listOf(
            50.dp,
            80.dp,
            110.dp,
            70.dp,
            100.dp,
            45.dp,
            55.dp
        )
    }

    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "Focus hours",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF081225)
                    )

                    Text(
                        text = "This week",
                        fontSize = 12.sp,
                        color = Color(0xFF7B8190)
                    )
                }

                Text(
                    text = "25h",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color(0xFF081225)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                heights.forEachIndexed { index, height ->

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {

                        Box(
                            modifier = Modifier
                                .width(22.dp)
                                .height(height)
                                .clip(CircleShape)
                                .background(Color(0xFFDDE3F7))
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = days[index],
                            fontSize = 11.sp,
                            color = Color(0xFF7B8190)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnalyticsMiniCard(
    stat: AnalyticsStat
) {

    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = stat.background
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(
                imageVector = stat.icon,
                contentDescription = null,
                tint = stat.accent,
                modifier = Modifier.size(20.dp)
            )

            Column {

                Text(
                    text = stat.value,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = stat.accent
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = stat.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = stat.accent.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun HabitConsistencyCard() {

    val rows = 6
    val columns = 8

    val activeCells = remember {
        setOf(
            Pair(0, 0), Pair(0, 1), Pair(0, 2),
            Pair(0, 4), Pair(0, 5),

            Pair(1, 0), Pair(1, 1),
            Pair(1, 4), Pair(1, 5),

            Pair(2, 0), Pair(2, 3),
            Pair(2, 4), Pair(2, 7),

            Pair(3, 2), Pair(3, 3),
            Pair(3, 6), Pair(3, 7),

            Pair(4, 2), Pair(4, 5),
            Pair(4, 6),

            Pair(5, 1), Pair(5, 2),
            Pair(5, 4), Pair(5, 5)
        )
    }

    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Habit consistency",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF081225)
                )

                Text(
                    text = "Last 8 weeks",
                    fontSize = 12.sp,
                    color = Color(0xFF7B8190)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                repeat(rows) { row ->

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        repeat(columns) { column ->

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (activeCells.contains(
                                                Pair(row, column)
                                            )
                                        )
                                            Color(0xFFAFC2FF)
                                        else
                                            Color(0xFFEFF2F8)
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Less",
                    fontSize = 11.sp,
                    color = Color(0xFF9AA3B2)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    repeat(5) { index ->

                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    when (index) {
                                        0 -> Color(0xFFEFF2F8)
                                        1 -> Color(0xFFDCE5FF)
                                        2 -> Color(0xFFC9D7FF)
                                        3 -> Color(0xFFB6C9FF)
                                        else -> Color(0xFFA3BBFF)
                                    }
                                )
                        )
                    }
                }

                Text(
                    text = "More",
                    fontSize = 11.sp,
                    color = Color(0xFF9AA3B2)
                )
            }
        }
    }
}