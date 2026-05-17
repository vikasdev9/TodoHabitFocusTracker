package com.example.todohabitfocus.feature.task.presentation.details

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.core.domain.model.Priority
import com.example.todohabitfocus.feature.task.presentation.TaskViewModel
import com.example.todohabitfocus.feature.task.presentation.components.CategoryChip
import com.example.todohabitfocus.feature.task.presentation.components.DateChip
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    taskId: String,
    onNavigateBack: () -> Unit,
    onEditTaskClick: (String) -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val task = remember(taskId, uiState.tasks) {
        uiState.tasks.find { it.id == taskId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { task?.let { viewModel.deleteTask(it); onNavigateBack() } }) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete")
                    }
                    IconButton(onClick = { onEditTaskClick(taskId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        task?.let { currentTask ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Image/Color Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            when(currentTask.priority) {
                                Priority.HIGH -> Color(0xFFFDEDF2)
                                Priority.MEDIUM -> Color(0xFFFEF5E7)
                                Priority.LOW -> Color(0xFFE8F1FF)
                            }
                        ),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadge(isCompleted = currentTask.isCompleted)
                        Spacer(modifier = Modifier.width(12.dp))
                        PriorityBadge(priority = currentTask.priority)
                    }
                }

                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(28.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = currentTask.title,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            CategoryChip(category = currentTask.category)
                            currentTask.dueDate?.let { DateChip(timestamp = it) }
                        }
                    }

                    DetailSection(
                        icon = Icons.Default.Description,
                        title = "Description",
                        content = if (currentTask.description.isEmpty()) "No description provided." else currentTask.description
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            DetailSection(
                                icon = Icons.Default.AccessTime,
                                title = "Created",
                                content = SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(currentTask.createdAt))
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            DetailSection(
                                icon = Icons.Default.Group,
                                title = "Assigned to",
                                content = "You"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                    
                    Button(
                        onClick = { viewModel.toggleTaskCompletion(currentTask) },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentTask.isCompleted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Icon(
                            if (currentTask.isCompleted) Icons.Default.Replay else Icons.Default.Check,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            if (currentTask.isCompleted) "Reopen Task" else "Complete Task",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun StatusBadge(isCompleted: Boolean) {
    val color = if (isCompleted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isCompleted) "Completed" else "In Progress",
                style = MaterialTheme.typography.labelLarge,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PriorityBadge(priority: Priority) {
    val color = when (priority) {
        Priority.HIGH -> Color.Red
        Priority.MEDIUM -> Color(0xFFFFA000)
        Priority.LOW -> Color.Blue
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = CircleShape
    ) {
        Text(
            text = priority.name,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DetailSection(icon: ImageVector, title: String, content: String) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
