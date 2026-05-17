package com.example.todohabitfocus.feature.task.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.todohabitfocus.core.domain.model.Priority
import com.example.todohabitfocus.core.domain.model.Task
import com.example.todohabitfocus.core.ui.components.ProgressRing
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier.padding(vertical = 4.dp),
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                    else -> Color.Transparent
                }, label = "dismissColor"
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.large)
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    ) {
        val backgroundColor = if (task.isCompleted) {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        } else {
            when (task.priority) {
                Priority.HIGH -> Color(0xFFFDEDF2) // Soft Pastel Red/Pink
                Priority.MEDIUM -> Color(0xFFFEF5E7) // Soft Pastel Amber
                Priority.LOW -> Color(0xFFE8F1FF) // Soft Pastel Blue
            }
        }

        val scale by animateFloatAsState(
            targetValue = if (dismissState.dismissDirection != null) 0.95f else 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            label = "scale"
        )

        val elevation by animateDpAsState(
            targetValue = if (dismissState.dismissDirection != null) 0.dp else 2.dp,
            animationSpec = tween(300),
            label = "elevation"
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale)
                .clickable { onClick() },
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomCheckbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggleComplete() }
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                        color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
                    )
                    
                    if (task.description.isNotEmpty() && !task.isCompleted) {
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        task.dueDate?.let {
                            DateChip(timestamp = it)
                        }
                        CategoryChip(category = task.category)
                    }
                }

                if (!task.isCompleted) {
                    PriorityIndicator(priority = task.priority)
                }
            }
        }
    }
}

@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val color by animateColorAsState(
        if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        label = "checkboxColor"
    )
    
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(if (checked) color else Color.Transparent)
            .clickable { onCheckedChange(!checked) }
            .then(if (!checked) Modifier.background(Color.White.copy(alpha = 0.5f)) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        } else {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(2.dp)
                .clip(CircleShape)
                .background(Color.White))
        }
    }
}

@Composable
fun TeamAvatars() {
    Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
        repeat(2) { index ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (index == 0) Color(0xFFFFCCBC) else Color(0xFFC8E6C9))
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (index == 0) "A" else "B",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DateChip(timestamp: Long) {
    val date = SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(timestamp))
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.CalendarToday,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = date,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun CategoryChip(category: String) {
    Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        shape = CircleShape
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PriorityIndicator(priority: Priority) {
    val color = when (priority) {
        Priority.HIGH -> Color.Red
        Priority.MEDIUM -> Color(0xFFFFA000)
        Priority.LOW -> Color.Blue
    }
    Box(
        modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(color)
    )
}
