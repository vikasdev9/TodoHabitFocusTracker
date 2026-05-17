package com.example.todohabitfocus.feature.task.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.core.ui.components.ModernTextField
import com.example.todohabitfocus.core.ui.components.ProgressRing
import com.example.todohabitfocus.feature.task.presentation.components.TaskCard

@Composable
fun TaskListRoute(
    onAddTaskClick: () -> Unit = {},
    onTaskClick: (String) -> Unit = {}
) {
    TaskListScreen(
        onAddTaskClick = onAddTaskClick,
        onTaskClick = onTaskClick
    )
}

@Composable
fun TaskListScreen(
    onAddTaskClick: () -> Unit,
    onTaskClick: (String) -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Personal", "Work", "Health", "Finance")

    val filteredTasks = remember(uiState.tasks, searchQuery, selectedCategory) {
        uiState.tasks.filter { task ->
            (selectedCategory == "All" || task.category == selectedCategory) &&
            (task.title.contains(searchQuery, ignoreCase = true) || 
             task.description.contains(searchQuery, ignoreCase = true))
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTaskClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    "Task Management",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Search Bar
                ModernTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Search tasks...",
                    leadingIcon = Icons.Default.Search
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedCategory == category,
                                borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }

            if (uiState.isLoading && uiState.tasks.isEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        com.example.todohabitfocus.core.ui.components.ShimmerEffect(
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            shape = MaterialTheme.shapes.extraLarge
                        )
                    }
                    items(5) {
                        com.example.todohabitfocus.core.ui.components.ShimmerEffect(
                            modifier = Modifier.fillMaxWidth().height(80.dp),
                            shape = MaterialTheme.shapes.large
                        )
                    }
                }
            } else if (!uiState.isLoading && filteredTasks.isEmpty()) {
                EmptyTasksState(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 64.dp))
            } else {
                com.example.todohabitfocus.core.ui.components.PullToRefreshWrapper(
                    isRefreshing = uiState.isLoading,
                    onRefresh = { viewModel.loadTasks() }
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            TaskSummarySection(
                                completedCount = uiState.tasks.count { it.isCompleted },
                                totalCount = uiState.tasks.size
                            )
                        }
                        
                        items(filteredTasks, key = { it.id }) { task ->
                            TaskCard(
                                task = task,
                                onClick = { onTaskClick(task.id) },
                                onToggleComplete = { viewModel.toggleTaskCompletion(task) },
                                onDelete = { viewModel.deleteTask(task) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskSummarySection(completedCount: Int, totalCount: Int) {
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
    
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Keep it up!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "$completedCount of $totalCount completed",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            ProgressRing(
                progress = progress,
                modifier = Modifier.size(64.dp),
                strokeWidth = 8f
            )
        }
    }
}

@Composable
fun EmptyTasksState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("No tasks found", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Get started by creating your first task", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
