package com.example.todohabitfocus.feature.onboarding.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.core.designsystem.theme.AppTheme
import com.example.todohabitfocus.core.ui.components.AppButton
import com.example.todohabitfocus.core.ui.components.ProgressRing
import com.example.todohabitfocus.core.ui.components.TaskCard
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    val colors = listOf(
        Color(0xFFFFEFEF), // Soft Red
        Color(0xFFEFFFFD), // Soft Cyan
        Color(0xFFF0F0FF)  // Soft Blue
    )

    val animatedBgColor by animateColorAsState(
        targetValue = colors[pagerState.currentPage],
        animationSpec = tween(500),
        label = "bgColor"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBgColor)
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
            0 -> HabitOnboardingContent()
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
    val habits = remember {
        listOf(
            HabitItem("Meditation", Icons.Default.SelfImprovement),
            HabitItem("Running", Icons.Default.DirectionsRun),
            HabitItem("Fitness", Icons.Default.FitnessCenter),
            HabitItem("Reading", Icons.Default.MenuBook),
            HabitItem("Productivity", Icons.Default.Bolt),
            HabitItem("Cycling", Icons.Default.DirectionsBike)
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "habitFloat")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .offset(y = floatAnim.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(habits) { habit ->
            Card(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = habit.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TaskOnboardingContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TaskCard(
            title = "Finalize UI Design",
            category = "Work",
            priority = "High",
            isCompleted = false,
            onToggle = {},
            onClick = {}
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Productivity", fontWeight = FontWeight.Bold)
                    Text("85%", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { 0.85f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            }
        }

        TaskCard(
            title = "Buy Groceries",
            category = "Personal",
            priority = "Medium",
            isCompleted = true,
            onToggle = {},
            onClick = {}
        )
    }
}

@Composable
fun FocusOnboardingContent() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        // Floating particles
        repeat(5) { i ->
            val angle = (i * 72f)
            val particleAlpha by infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 0.8f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000 + i * 200, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "alpha$i"
            )
            
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .offset(x = (80 * kotlin.math.cos(angle)).dp, y = (80 * kotlin.math.sin(angle)).dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = particleAlpha))
            )
        }

        ProgressRing(
            progress = 0.75f,
            modifier = Modifier
                .size(200.dp)
                .scale(scale),
            strokeWidth = 12f
        )
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "25:00",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "FOCUS",
                style = MaterialTheme.typography.labelLarge,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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

        // Dots
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
