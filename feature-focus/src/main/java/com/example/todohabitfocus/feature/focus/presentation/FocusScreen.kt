package com.example.todohabitfocus.feature.focus.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.core.domain.model.FocusSession
import com.example.todohabitfocus.feature.focus.presentation.components.CircularTimer
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FocusScreen(
    viewModel: FocusViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }
    
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFE0F2FE), Color(0xFFF0F9FF)) // Calming Soft Blue
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(initialOffsetY = { 40 })
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 32.dp)
            ) {
                item {
                    FocusHeader()
                }
                
                item {
                    Spacer(modifier = Modifier.height(48.dp))

                    val totalDuration = 25 * 60 * 1000L // Simplified for now as currentSessionType was missing

                    val progress = if (uiState.isRunning) {
                         uiState.timeLeftMillis.toFloat() / totalDuration
                    } else 1f

                    CircularTimer(
                        progress = progress,
                        timeLeft = formatTime(uiState.timeLeftMillis),
                        isRunning = uiState.isRunning
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(48.dp))
                    SessionTypeSelector(
                        selectedType = uiState.currentType.name,
                        onTypeSelected = { 
                            when(it) {
                                "POMODORO" -> viewModel.startPomodoro()
                                "SHORT_BREAK" -> viewModel.startShortBreak()
                                // "LONG_BREAK" -> viewModel.startLongBreak()
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(48.dp))
                    FocusControls(
                        isRunning = uiState.isRunning,
                        onStart = { viewModel.startPomodoro() },
                        onStop = { viewModel.stopTimer() }
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(48.dp))
                    FocusStatsSummary(totalFocusTime = uiState.totalFocusTime)
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Focus History",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1E293B)
                        )
                        TextButton(onClick = { /* Navigate to History */ }) {
                            Text("See All")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(uiState.sessions.take(3)) { session ->
                    PremiumSessionItem(session)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                
                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
fun FocusHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Stay Focused",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0369A1)
        )
        Text(
            text = "Find your inner calm and productivity",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF0C4A6E).copy(alpha = 0.6f)
        )
    }
}

@Composable
fun SessionTypeSelector(selectedType: String, onTypeSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.5f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        listOf("POMODORO", "SHORT_BREAK", "LONG_BREAK").forEach { type ->
            val isSelected = selectedType == type
            val label = type.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) Color.White else Color.Transparent)
                    .clickable { onTypeSelected(type) }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) Color(0xFF0369A1) else Color(0xFF0369A1).copy(alpha = 0.6f),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun FocusControls(isRunning: Boolean, onStart: () -> Unit, onStop: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isRunning) {
            ExtendedFloatingActionButton(
                onClick = onStart,
                containerColor = Color(0xFF0284C7),
                contentColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.width(160.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Focus", fontWeight = FontWeight.Bold)
            }
        } else {
            ExtendedFloatingActionButton(
                onClick = onStop,
                containerColor = Color(0xFFEF4444),
                contentColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.width(160.dp)
            ) {
                Icon(Icons.Default.Stop, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("End Session", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FocusStatsSummary(totalFocusTime: Long) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Total Focus Today",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = formatDuration(totalFocusTime),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF0369A1),
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFFE0F2FE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Timer, contentDescription = null, tint = Color(0xFF0369A1))
            }
        }
    }
}

@Composable
fun PremiumSessionItem(session: FocusSession) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFBAE6FD)),
                contentAlignment = Alignment.Center
            ) {
                Text("🧘", fontSize = 24.sp)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.type.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1E293B),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(session.startTime)),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B)
                )
            }
            
            Text(
                text = "+${session.durationMillis / 60000} min",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF0284C7),
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun SessionItem(session: FocusSession) {
    Surface(
        color = Color.White.copy(alpha = 0.03f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = session.type.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(session.startTime)),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.4f)
                )
            }
            Text(
                text = "${session.durationMillis / 60000}m",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF3F8CFF),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LargeIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    containerColor: Color
) {
    FilledIconButton(
        onClick = onClick,
        modifier = Modifier.size(80.dp),
        shape = CircleShape,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = containerColor,
            contentColor = Color.White
        )
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(40.dp))
    }
}

private fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

private fun formatDuration(millis: Long): String {
    val minutes = millis / (1000 * 60)
    return if (minutes < 60) {
        "${minutes}m"
    } else {
        "${minutes / 60}h ${minutes % 60}m"
    }
}
