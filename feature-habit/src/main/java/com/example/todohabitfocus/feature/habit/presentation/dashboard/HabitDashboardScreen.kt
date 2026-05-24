package com.example.todohabitfocus.feature.habit.presentation.dashboard

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
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.feature.habit.presentation.HabitViewModel
import com.example.todohabitfocus.feature.habit.presentation.components.PremiumHabitCard

@Immutable
data class HabitUi(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val bgColor: Color,
    val iconColor: Color,
    val streak: Int,
    val progress: Int
)

@Composable
fun HabitDashboardScreen(
    habits: List<HabitUi> = sampleHabits(),
    onAddHabitClick: () -> Unit = {},
    onHabitClick: (Int) -> Unit = {}
) {

    val hasHabits by remember(habits) {
        derivedStateOf { habits.isNotEmpty() }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7FB))
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(
            top = 12.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {

        // HEADER
        item {
            HabitsHeader(onAddHabitClick = onAddHabitClick)
        }

        if (hasHabits) {

            // WEEKLY TRACKER
            item {
                WeeklyTrackerCard()
            }

            // SECTION TITLE
            item {
                Text(
                    text = "ALL HABITS",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = Color(0xFF7B8190)
                )
            }

            // HABITS GRID
            item {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(540.dp),
                    userScrollEnabled = false,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = habits,
                        key = { it.id }
                    ) { habit ->

                        HabitCard(
                            habit = habit,
                            onClick = { onHabitClick(habit.id) }
                        )
                    }
                }
            }

            // QUICK LOG TITLE
            item {

                Text(
                    text = "QUICK LOG",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = Color(0xFF7B8190)
                )
            }

            // WATER CARD
            item {
                WaterIntakeCard()
            }

        } else {

            item {
                EmptyHabitPlaceholder()
            }
        }
    }
}

@Composable
fun HabitsHeader(
    onAddHabitClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        Column {

            Text(
                text = "Habits",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = Color(0xFF081225)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "6 active routines",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF7B8190)
            )
        }

        FloatingActionButton(
            onClick = onAddHabitClick,
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
fun WeeklyTrackerCard() {

    val week = remember {
        listOf("M", "T", "W", "T", "F", "S", "S")
    }

    val completed = remember {
        listOf(
            false, false, false,
            false, false, false, false
        )
    }

    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "This week",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF081225)
                )

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFFFE4D6))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {

                    Text(
                        text = "🔥 28 day streak",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD96C3D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                week.forEachIndexed { index, day ->

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    if (completed[index])
                                        Color(0xFF3554D1)
                                    else
                                        Color(0xFFE9EEF8)
                                )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = day,
                            fontSize = 11.sp,
                            color = Color(0xFF081225),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HabitCard(
    habit: HabitUi,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .height(165.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = habit.bgColor
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = habit.icon,
                    contentDescription = null,
                    tint = habit.iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = habit.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = habit.iconColor
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    repeat(6) { index ->

                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index < habit.progress)
                                        habit.iconColor
                                    else
                                        Color.White.copy(alpha = 0.5f)
                                )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = habit.iconColor,
                            modifier = Modifier.size(12.dp)
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        Text(
                            text = habit.streak.toString(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = habit.iconColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WaterIntakeCard() {

    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDDF3FF)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = Color(0xFF0066B2),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Water intake",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0066B2)
                    )

                    Text(
                        text = "5 of 8 glasses today",
                        fontSize = 13.sp,
                        color = Color(0xFF5B88B2)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.7f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {

                    Text(
                        text = "+1",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0066B2)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                repeat(8) { index ->

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (index < 5)
                                    Color(0xFF0066B2)
                                else
                                    Color.White.copy(alpha = 0.5f)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyHabitPlaceholder() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Default.Spa,
                contentDescription = null,
                tint = Color(0xFF8BC34A),
                modifier = Modifier.size(54.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No habits tracked yet",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = Color(0xFF20222B)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Start your first small habit today!",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF7B8190)
        )
    }
}

fun sampleHabits(): List<HabitUi> {

    return listOf(

        HabitUi(
            id = 1,
            title = "Meditation",
            icon = Icons.Default.SelfImprovement,
            bgColor = Color(0xFFE8DDF8),
            iconColor = Color(0xFF6C4AB6),
            streak = 12,
            progress = 2
        ),

        HabitUi(
            id = 2,
            title = "Running",
            icon = Icons.Default.ShowChart,
            bgColor = Color(0xFFF9DDCF),
            iconColor = Color(0xFFD96C3D),
            streak = 4,
            progress = 3
        ),

        HabitUi(
            id = 3,
            title = "Fitness",
            icon = Icons.Default.FitnessCenter,
            bgColor = Color(0xFFD8F3E4),
            iconColor = Color(0xFF1F7A5C),
            streak = 28,
            progress = 6
        ),

        HabitUi(
            id = 4,
            title = "Reading",
            icon = Icons.Default.MenuBook,
            bgColor = Color(0xFFF6E9B9),
            iconColor = Color(0xFF8C6A00),
            streak = 9,
            progress = 5
        ),

        HabitUi(
            id = 5,
            title = "Breathing",
            icon = Icons.Default.Air,
            bgColor = Color(0xFFD9EEF9),
            iconColor = Color(0xFF2979A8),
            streak = 21,
            progress = 6
        ),

        HabitUi(
            id = 6,
            title = "Cycling",
            icon = Icons.Default.DirectionsBike,
            bgColor = Color(0xFFF8D9DF),
            iconColor = Color(0xFFC44569),
            streak = 2,
            progress = 1
        )
    )
}