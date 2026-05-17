package com.example.todohabitfocus.feature.focus.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularTimer(
    progress: Float,
    timeLeft: String,
    isRunning: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isRunning) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Canvas(modifier = Modifier.size(280.dp)) {
            val strokeWidth = 12.dp.toPx()
            val center = Offset(size.width / 2, size.height / 2)
            val radius = (size.width - strokeWidth) / 2

            // Background Circle
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth)
            )

            // Progress Arc
            drawArc(
                color = Color(0xFF3F8CFF),
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = timeLeft,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 64.sp * pulse,
                    fontWeight = FontWeight.Light,
                    color = Color.White
                )
            )
            Text(
                text = if (isRunning) "FOCUSING" else "READY",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.6f),
                letterSpacing = 4.sp
            )
        }
        
        // Particles (Optional visualization)
        if (isRunning) {
            FloatingParticles()
        }
    }
}

@Composable
fun FloatingParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    
    Box(modifier = Modifier.size(300.dp)) {
        repeat(5) { index ->
            val angle = (index * 72f) * (PI.toFloat() / 180f)
            val offset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 20f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000 + index * 200, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "offset"
            )
            
            val x = (150f + (140f + offset) * cos(angle)).dp
            val y = (150f + (140f + offset) * sin(angle)).dp
            
            Box(
                modifier = Modifier
                    .offset(x, y)
                    .size(4.dp)
                    .background(Color.White.copy(alpha = 0.3f), androidx.compose.foundation.shape.CircleShape)
            )
        }
    }
}
