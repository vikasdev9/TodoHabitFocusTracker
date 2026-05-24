package com.example.todohabitfocus.feature.task.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.core.ui.components.ModernTextField
import com.example.todohabitfocus.core.ui.components.ProgressRing

@Immutable
data class TaskUi(
    val id: Int,
    val category: String,
    val title: String,
    val progress: Float,
    val progressText: String,
    val day: String,
    val priority: String,
    val background: Color,
    val accent: Color
)
@Composable
fun TaskListRoute(
    onAddTaskClick: () -> Unit = {},
    onTaskClick: (String) -> Unit = {}
) {
    TaskListScreen(
        onAddTaskClick = onAddTaskClick,
        onTaskClick = onTaskClick
    )
}

@Composable
fun TaskListScreen(
    tasksInProgress: List<TaskUi> = remember { sampleInProgressTasks() },
    upcomingTasks: List<TaskUi> = remember { sampleUpcomingTasks() },
    onAddTaskClick: () -> Unit = {},
    onTaskClick: (String) -> Unit = {}
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7FB))
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // HEADER
        item {
            TaskDashboardHeader(onAddTaskClick = onAddTaskClick)
        }

        // SEARCH + FILTER
        item {
            SearchAndFilterSection()
        }

        // IN PROGRESS
        item {

            SectionHeader(
                title = "IN PROGRESS",
                count = tasksInProgress.size
            )
        }

        items(
            items = tasksInProgress,
            key = { it.id }
        ) { task ->

            TaskCard(
                task = task,
                onClick = { onTaskClick(task.id.toString()) }
            )
        }

        // UP NEXT
        item {

            SectionHeader(
                title = "UP NEXT",
                count = upcomingTasks.size
            )
        }

        items(
            items = upcomingTasks,
            key = { it.id }
        ) { task ->

            TaskCard(
                task = task,
                onClick = { onTaskClick(task.id.toString()) }
            )
        }
    }
}

@Composable
fun TaskDashboardHeader(
    onAddTaskClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        Column {

            Text(
                text = "My Tasks",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = Color(0xFF081225)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "5 active tasks",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF7B8190)
            )
        }

        FloatingActionButton(
            onClick = onAddTaskClick,
            modifier = Modifier.size(54.dp),
            shape = CircleShape,
            containerColor = Color(0xFF3554D1),
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun SearchAndFilterSection() {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // SEARCH
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        "Search...",
                        color = Color(0xFF9AA3B2)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF9AA3B2)
                    )
                },
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = Color(0xFF081225)
                )
            }
        }

        // FILTERS
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            listOf(
                "All",
                "Today",
                "Upcoming",
                "Completed"
            ).forEachIndexed { index, title ->

                val selected = index == 0

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (selected)
                                Color(0xFF3554D1)
                            else
                                Color.White
                        )
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {

                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (selected)
                            Color.White
                        else
                            Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    count: Int
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            ),
            color = Color(0xFF6B7280)
        )

        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF9AA3B2)
        )
    }
}

@Composable
fun TaskCard(
    task: TaskUi,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = task.background
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

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = task.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = task.accent.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = task.accent
                    )
                }

                ProgressRing(
                    progress = task.progress,
                    progressText = task.progressText,
                    color = task.accent
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // AVATARS
                Row {

                    listOf(
                        Color(0xFFF4A261),
                        Color(0xFFE9C46A),
                        Color(0xFF6FCF97)
                    ).forEachIndexed { index, color ->

                        Box(
                            modifier = Modifier
                                .offset(x = (-index * 6).dp)
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    2.dp,
                                    Color.White,
                                    CircleShape
                                )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .offset(x = (-18).dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "+1",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7B8190)
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    PriorityChip(
                        text = task.priority
                    )

                    DayChip(
                        text = task.day
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressRing(
    progress: Float,
    progressText: String,
    color: Color
) {

    Box(
        modifier = Modifier.size(54.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.size(54.dp)
        ) {

            drawArc(
                color = Color.White.copy(alpha = 0.5f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = progress * 360f,
                useCenter = false,
                style = Stroke(
                    width = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }

        Text(
            text = progressText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun PriorityChip(
    text: String
) {

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.7f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {

        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD96C3D)
        )
    }
}

@Composable
fun DayChip(
    text: String
) {

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.7f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {

        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6C4AB6)
        )
    }
}

fun sampleInProgressTasks(): List<TaskUi> {

    return listOf(

        TaskUi(
            id = 1,
            category = "DESIGN",
            title = "Mobile app redesign",
            progress = 0.72f,
            progressText = "72%",
            day = "Today",
            priority = "",
            background = Color(0xFFE8DDF8),
            accent = Color(0xFF6C4AB6)
        ),

        TaskUi(
            id = 2,
            category = "RESEARCH",
            title = "User interviews — Q3",
            progress = 0.45f,
            progressText = "45%",
            day = "Thu",
            priority = "",
            background = Color(0xFFD9EEF9),
            accent = Color(0xFF2979A8)
        )
    )
}

fun sampleUpcomingTasks(): List<TaskUi> {

    return listOf(

        TaskUi(
            id = 3,
            category = "MARKETING",
            title = "Launch campaign assets",
            progress = 0.20f,
            progressText = "20%",
            day = "Tomorrow",
            priority = "High",
            background = Color(0xFFF9DDCF),
            accent = Color(0xFFD96C3D)
        ),

        TaskUi(
            id = 4,
            category = "ENGINEERING",
            title = "API v2 endpoints",
            progress = 0.10f,
            progressText = "10%",
            day = "Fri",
            priority = "",
            background = Color(0xFFD8F3E4),
            accent = Color(0xFF1F7A5C)
        ),

        TaskUi(
            id = 5,
            category = "OPS",
            title = "Quarterly planning doc",
            progress = 0f,
            progressText = "0%",
            day = "Mon",
            priority = "Medium",
            background = Color(0xFFF6E9B9),
            accent = Color(0xFF8C6A00)
        )
    )
}