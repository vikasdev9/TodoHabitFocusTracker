package com.example.todohabitfocus.feature.analytics.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todohabitfocus.feature.analytics.domain.model.ProductivityPoint

@Composable
fun LineChart(
    data: List<ProductivityPoint>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary
) {
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animationProgress.animateTo(1f, tween(1500))
    }

    Canvas(modifier = modifier) {
        if (data.isEmpty()) return@Canvas

        val width = size.width
        val height = size.height
        val maxVal = data.maxOf { it.value }.coerceAtLeast(1f)
        val stepX = width / (data.size - 1)

        val path = Path()
        data.forEachIndexed { i, point ->
            val x = i * stepX
            val y = height - (point.value / maxVal * height * animationProgress.value)
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx())
        )

        // Fill area under line
        val fillPath = Path().apply {
            addPath(path)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(lineColor.copy(alpha = 0.3f), Color.Transparent)
            )
        )
    }
}

@Composable
fun PremiumBarChart(
    data: List<ProductivityPoint>,
    modifier: Modifier = Modifier
) {
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animationProgress.animateTo(1f, tween(1200, easing = FastOutSlowInEasing))
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        val maxVal = data.maxOf { it.value }.coerceAtLeast(1f)
        data.forEach { point ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(point.value / maxVal * animationProgress.value)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                )
                            )
                        )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = point.label.take(3),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun ConsistencyCard(
    name: String,
    percentage: Float,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Bold)
            Text("${(percentage * 100).toInt()}% Consistency", fontSize = 12.sp, color = Color.Gray)
        }
        
        Box(contentAlignment = Alignment.Center) {
            androidx.compose.material3.CircularProgressIndicator(
                progress = percentage,
                color = color,
                trackColor = color.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            )
            Text("${(percentage * 100).toInt()}%", fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}
