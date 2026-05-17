package com.example.todohabitfocus.feature.task.presentation.add_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import java.text.SimpleDateFormat
import java.util.*

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
    var dueDate by remember(existingTask) { mutableLongStateOf(existingTask?.dueDate ?: System.currentTimeMillis()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (taskId == null) "Create Task" else "Edit Task", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp,
                shadowElevation = 16.dp
            ) {
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
                                    category = category,
                                    dueDate = dueDate
                                ))
                            }
                        }
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(56.dp),
                    enabled = title.isNotBlank(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Save Task", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            // Task Title
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Task Title", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                ModernTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "What are you planning?",
                    leadingIcon = Icons.Default.Edit
                )
            }

            // Priority Selector
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Priority Level", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
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
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) color.copy(alpha = 0.15f) else Color.White)
                                .clickable { priority = p }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                p.name,
                                color = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Date Picker (Mock Button)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Due Date", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Surface(
                    modifier = Modifier.fillMaxWidth().clickable { /* Show Date Picker */ },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(dueDate)),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            // Upload Section (Mock)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Attachments", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Surface(
                    modifier = Modifier.fillMaxWidth().height(100.dp).clickable { /* Mock Upload */ },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = FilterChipDefaults.filterChipBorder(enabled = true, selected = false, borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
                        Text("Upload files or images", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                    }
                }
            }

            // Category Selector
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Category", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
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
                                selectedLabelColor = Color.White,
                                containerColor = Color.White
                            )
                        )
                    }
                }
            }

            // Description
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Additional Notes", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                ModernTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = "Add some details...",
                    modifier = Modifier.height(150.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
