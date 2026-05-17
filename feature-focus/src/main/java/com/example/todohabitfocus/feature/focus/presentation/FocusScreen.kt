package com.example.todohabitfocus.feature.focus.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(24.dp)
        ) {
            item {
                Text(
                    text = "Focus Session",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(64.dp))

                val progress = if (uiState.isRunning) {
                     uiState.timeLeftMillis.toFloat() / (25 * 60 * 1000L)
                } else 1f

                CircularTimer(
                    progress = progress,
                    timeLeft = formatTime(uiState.timeLeftMillis),
                    isRunning = uiState.isRunning
                )
            }

            item {
                Spacer(modifier = Modifier.height(64.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!uiState.isRunning) {
                        LargeIconButton(
                            icon = Icons.Default.PlayArrow,
                            onClick = { viewModel.startPomodoro() },
                            containerColor = Color(0xFF3F8CFF)
                        )
                    } else {
                        LargeIconButton(
                            icon = Icons.Default.Stop,
                            onClick = { viewModel.stopTimer() },
                            containerColor = Color(0xFFFF4B4B)
                        )
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(48.dp))
                
                Surface(
                    color = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "TOTAL FOCUSED TODAY",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.4f),
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = formatDuration(uiState.totalFocusTime),
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Recent Sessions",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(uiState.sessions.take(5)) { session ->
                SessionItem(session)
                Spacer(modifier = Modifier.height(8.dp))
            }
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
