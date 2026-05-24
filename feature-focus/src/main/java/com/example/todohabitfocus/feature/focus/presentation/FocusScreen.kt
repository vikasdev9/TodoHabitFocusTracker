package com.example.todohabitfocus.feature.focus.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.core.domain.model.FocusSession
import com.example.todohabitfocus.feature.focus.presentation.components.CircularTimer
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FocusScreen() {

    val infiniteTransition = rememberInfiniteTransition(label = "breathing")

    // BREATHING SCALE
    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2400,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathingScale"
    )

    // OUTER GLOW
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2400,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    val modes = remember {
        listOf(
            FocusModeUi(
                title = "Pomodoro",
                duration = "25 min",
                icon = Icons.Default.Psychology,
                selected = true
            ),
            FocusModeUi(
                title = "Deep work",
                duration = "50 min",
                icon = Icons.Default.Bolt,
                selected = false
            ),
            FocusModeUi(
                title = "Short break",
                duration = "5 min",
                icon = Icons.Default.Coffee,
                selected = false
            ),
            FocusModeUi(
                title = "Long break",
                duration = "15 min",
                icon = Icons.Default.DarkMode,
                selected = false
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FC))
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(
            top = 20.dp,
            bottom = 28.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // TITLE
        item {

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Focus session",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF7B8190)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Stay in flow",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = Color(0xFF081225)
            )
        }

        // TIMER
        item {

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier.size(360.dp),
                contentAlignment = Alignment.Center
            ) {

                // OUTER BREATHING GLOW
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .scale(glowScale)
                        .clip(CircleShape)
                        .background(
                            Color(0xFFEEF1FF)
                        )
                )

                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .scale(glowScale * 0.96f)
                        .clip(CircleShape)
                        .background(
                            Color(0xFFF3F5FF)
                        )
                )

                // RING
                Canvas(
                    modifier = Modifier
                        .size(260.dp)
                        .scale(breathingScale)
                ) {

                    drawArc(
                        color = Color(0xFFEAEFFD),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(
                            width = 14.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )

                    drawArc(
                        color = Color(0xFF172B6A),
                        startAngle = -90f,
                        sweepAngle = 220f,
                        useCenter = false,
                        style = Stroke(
                            width = 14.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }

                // CENTER TEXT
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "POMODORO",
                        letterSpacing = 4.sp,
                        fontSize = 16.sp,
                        color = Color(0xFF7B8190)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "25:00",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Color(0xFF081225)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Session 3 of 6",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF7B8190)
                    )
                }
            }
        }

        // CONTROLS
        item {

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // RESET
                CircleActionButton(
                    icon = Icons.Default.Refresh,
                    size = 60.dp,
                    background = Color.White,
                    tint = Color(0xFF081225)
                )

                // PLAY
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF172B6A))
                        .shadow(
                            elevation = 24.dp,
                            shape = CircleShape,
                            spotColor = Color(0x33172B6A)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // ADD TIME
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "+5m",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF081225)
                    )
                }
            }
        }

        // MODES
        item {

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {

                Text(
                    text = "Modes",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF6B7280)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        // MODES GRID
        item {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(190.dp),
                userScrollEnabled = false,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(
                    items = modes,
                    key = { it.title }
                ) { mode ->

                    FocusModeCard(mode)
                }
            }
        }
    }
}

@Immutable
data class FocusModeUi(
    val title: String,
    val duration: String,
    val icon: ImageVector,
    val selected: Boolean
)

@Composable
fun FocusModeCard(
    mode: FocusModeUi
) {

    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (mode.selected)
                Color(0xFF172B6A)
            else
                Color.White
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        if (mode.selected)
                            Color.White.copy(alpha = 0.12f)
                        else
                            Color(0xFFE3ECFF)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = mode.icon,
                    contentDescription = null,
                    tint = if (mode.selected)
                        Color.White
                    else
                        Color(0xFF172B6A),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {

                Text(
                    text = mode.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (mode.selected)
                        Color.White
                    else
                        Color(0xFF081225)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = mode.duration,
                    fontSize = 14.sp,
                    color = if (mode.selected)
                        Color.White.copy(alpha = 0.7f)
                    else
                        Color(0xFF7B8190)
                )
            }
        }
    }
}

@Composable
fun CircleActionButton(
    icon: ImageVector,
    size: Dp,
    background: Color,
    tint: Color
) {

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(34.dp)
        )
    }
}
