package com.example.todohabitfocus.feature.onboarding.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.core.ui.components.AppButton
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FC))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingPage(page)
            }

            OnboardingBottomBar(
                currentPage = pagerState.currentPage,
                onSkip = {
                    viewModel.completeOnboarding()
                    onFinish()
                },
                onNext = {
                    if (pagerState.currentPage < 2) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    } else {
                        viewModel.completeOnboarding()
                        onFinish()
                    }
                }
            )
        }
    }
}

@Composable
fun OnboardingPage(page: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        when (page) {
            0 -> HabitOnboardingContent( )
            1 -> TaskOnboardingContent()
            2 -> FocusOnboardingContent()
        }

        Spacer(modifier = Modifier.weight(1f))

        val title = when (page) {
            0 -> "Cultivate Better Habits"
            1 -> "Master Your Productivity"
            else -> "Deep Focus, Better Work"
        }
        val description = when (page) {
            0 -> "Track your daily routines with elegant streaks and reminders."
            1 -> "Organize tasks by priority and monitor your progress visually."
            else -> "Use scientific focus sessions to achieve your goals faster."
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

data class HabitItem(val name: String, val icon: ImageVector)

@Composable
fun HabitOnboardingContent() {

    data class HabitUi(
        val title: String,
        val icon: ImageVector,
        val bgColor: Color,
        val iconColor: Color
    )

    val habits = listOf(
        HabitUi(
            "Meditation",
            Icons.Default.SelfImprovement,
            Color(0xFFE8DDF8),
            Color(0xFF6C4AB6)
        ),
        HabitUi(
            "Running",
            Icons.Default.DirectionsRun,
            Color(0xFFF9DDCF),
            Color(0xFFD96C3D)
        ),
        HabitUi(
            "Fitness",
            Icons.Default.FitnessCenter,
            Color(0xFFD8F3E4),
            Color(0xFF1F7A5C)
        ),
        HabitUi(
            "Reading",
            Icons.Default.MenuBook,
            Color(0xFFF6E9B9),
            Color(0xFF8C6A00)
        ),
        HabitUi(
            "Breathing",
            Icons.Default.Air,
            Color(0xFFD9EEF9),
            Color(0xFF2979A8)
        ),
        HabitUi(
            "Cycling",
            Icons.Default.DirectionsBike,
            Color(0xFFF8D9DF),
            Color(0xFFC44569)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                userScrollEnabled = false
            ) {

                items(habits) { habit ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.2f),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = habit.bgColor
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 0.dp
                        )
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
                                color = habit.iconColor,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }
        }

        Column {

            Text(
                text = "01 · HABITS",
                color = Color(0xFF7A7FE2),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Build better\nhabits, daily.",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 42.sp
                ),
                color = Color(0xFF0B132B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Track routines and maintain powerful streaks every single day.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF6B7280),
                lineHeight = 26.sp
            )
        }
    }
}

@Composable
fun TaskOnboardingContent() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .height(120.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = 0.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE6D9FF)
                    ),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {}

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.82f)
                        .height(120.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = 10.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFCDEFFF)
                    ),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {}

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .align(Alignment.BottomCenter),
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFD9C8))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "HIGH",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD86D3E)
                                )
                            }

                            Box(
                                contentAlignment = Alignment.Center
                            ) {

                                CircularProgressIndicator(
                                    progress = { 0.68f },
                                    modifier = Modifier.size(38.dp),
                                    strokeWidth = 4.dp,
                                    color = Color(0xFF3554D1),
                                    trackColor = Color(0xFFE9EDFF)
                                )

                                Text(
                                    text = "68%",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF3554D1)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "Design system v2",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFF0B132B)
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "8 of 12 subtasks complete",
                                fontSize = 13.sp,
                                color = Color(0xFF7B8190)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

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
                            }

                            Row(
                                modifier = Modifier.wrapContentWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = Color(0xFF7B8190)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "Today",
                                    fontSize = 12.sp,
                                    color = Color(0xFF7B8190),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFCFF4DF)
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF1B7F5A),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Morning standup",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF126B4A)
                        )

                        Text(
                            text = "Completed · 9:30 AM",
                            fontSize = 12.sp,
                            color = Color(0xFF3B7C63)
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color(0xFF3B7C63),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF4CC)),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            Text(
                                text = "JUN",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB58A00)
                            )

                            Text(
                                text = "14",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB58A00)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Design review",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0B132B)
                        )

                        Text(
                            text = "Tomorrow · 11:00 AM",
                            fontSize = 12.sp,
                            color = Color(0xFF7B8190)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFDDF3FF))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {

                        Text(
                            text = "MED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2196F3)
                        )
                    }
                }
            }
        }

        Column {

            Text(
                text = "02 · TASKS",
                color = Color(0xFF7A7FE2),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Organize\nyour tasks.",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 42.sp
                ),
                color = Color(0xFF081225)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Manage todos, deadlines and projects with calm clarity.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF6B7280),
                lineHeight = 26.sp
            )
        }
    }
}

@Composable
fun FocusOnboardingContent() {

    val infiniteTransition = rememberInfiniteTransition(label = "focus_animation")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2400,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val glowScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2400,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .size(260.dp),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .scale(glowScale)
                        .clip(CircleShape)
                        .background(
                            Color(0xFF4C6FFF).copy(alpha = 0.05f)
                        )
                )

                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .scale(glowScale * 0.96f)
                        .clip(CircleShape)
                        .background(
                            Color(0xFF4C6FFF).copy(alpha = 0.04f)
                        )
                )

                Canvas(
                    modifier = Modifier
                        .size(190.dp)
                        .scale(scale)
                ) {

                    drawArc(
                        color = Color(0xFFE8EBF5),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(
                            width = 14.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )

                    drawArc(
                        color = Color(0xFF3554D1),
                        startAngle = 140f,
                        sweepAngle = 290f,
                        useCenter = false,
                        style = Stroke(
                            width = 14.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "24:18",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Color(0xFF0B132B)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = Color(0xFFFF8A65),
                            modifier = Modifier.size(14.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "Deep Work",
                            color = Color(0xFF6B7280),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                FocusStatCard(
                    modifier = Modifier.weight(1f),
                    title = "TODAY",
                    value = "2h 40m",
                    bgColor = Color(0xFFDDF3FF),
                    textColor = Color(0xFF0066B2)
                )

                FocusStatCard(
                    modifier = Modifier.weight(1f),
                    title = "STREAK",
                    value = "12 days",
                    bgColor = Color(0xFFFFF1CC),
                    textColor = Color(0xFFB7791F)
                )

                FocusStatCard(
                    modifier = Modifier.weight(1f),
                    title = "SESSIONS",
                    value = "5",
                    bgColor = Color(0xFFDDF7E7),
                    textColor = Color(0xFF1B7F5A)
                )
            }
        }

        Column {

            Text(
                text = "03 · FOCUS",
                color = Color(0xFF7A7FE2),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Stay deeply\nfocused.",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 42.sp
                ),
                color = Color(0xFF081225)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Run pomodoro sessions and protect your deep work time.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF6B7280),
                lineHeight = 28.sp
            )
        }
    }
}

@Composable
fun FocusStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    bgColor: Color,
    textColor: Color
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = bgColor
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = textColor.copy(alpha = 0.7f),
                letterSpacing = 1.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                maxLines = 1
            )
        }
    }
}

@Composable
fun OnboardingBottomBar(
    currentPage: Int,
    onSkip: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (currentPage < 2) {
            TextButton(onClick = onSkip) {
                Text(
                    "Skip",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Spacer(modifier = Modifier.width(60.dp))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(3) { index ->
                val width by animateDpAsState(
                    targetValue = if (currentPage == index) 24.dp else 8.dp,
                    label = "dotWidth"
                )
                Box(
                    modifier = Modifier
                        .size(height = 8.dp, width = width)
                        .clip(CircleShape)
                        .background(
                            if (currentPage == index) MaterialTheme.colorScheme.primary 
                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                )
            }
        }

        if (currentPage < 2) {
            FloatingActionButton(
                onClick = onNext,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
            }
        } else {
            AppButton(
                text = "Get Started",
                onClick = onNext,
                modifier = Modifier.width(140.dp)
            )
        }
    }
}
