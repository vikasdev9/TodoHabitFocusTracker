package com.example.todohabitfocus.feature.task.presentation.add_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.core.domain.model.Priority
import com.example.todohabitfocus.core.ui.components.ModernTextField
import com.example.todohabitfocus.feature.task.presentation.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditTaskScreen(
    taskId: String? = null,
    onNavigateBack: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val existingTask = remember(taskId, uiState.tasks) {
        uiState.tasks.find { it.id == taskId }
    }

    var title by remember(existingTask) { mutableStateOf(existingTask?.title ?: "") }
    var description by remember(existingTask) { mutableStateOf(existingTask?.description ?: "") }
    var priority by remember(existingTask) { mutableStateOf(existingTask?.priority ?: Priority.MEDIUM) }
    var category by remember(existingTask) { mutableStateOf(existingTask?.category ?: "Personal") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (taskId == null) "New Task" else "Edit Task", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            if (taskId == null) {
                                viewModel.addTask(title, description, priority, category)
                            } else {
                                existingTask?.let {
                                    viewModel.updateTask(it.copy(
                                        title = title,
                                        description = description,
                                        priority = priority,
                                        category = category
                                    ))
                                }
                            }
                            onNavigateBack()
                        },
                        enabled = title.isNotBlank(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Save")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("What needs to be done?", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                ModernTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "Task title",
                    leadingIcon = Icons.Default.Edit
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Description", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                ModernTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = "Add some details...",
                    modifier = Modifier.height(120.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Priority", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Priority.entries.forEach { p ->
                        val isSelected = priority == p
                        val color = when(p) {
                            Priority.HIGH -> Color.Red
                            Priority.MEDIUM -> Color(0xFFFFA000)
                            Priority.LOW -> Color.Blue
                        }
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(MaterialTheme.shapes.medium)
                                .background(if (isSelected) color.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface)
                                .clickable { priority = p }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                p.name,
                                color = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Category", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                val categories = listOf("Personal", "Work", "Health", "Finance")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { c ->
                        val isSelected = category == c
                        FilterChip(
                            selected = isSelected,
                            onClick = { category = c },
                            label = { Text(c) },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}
