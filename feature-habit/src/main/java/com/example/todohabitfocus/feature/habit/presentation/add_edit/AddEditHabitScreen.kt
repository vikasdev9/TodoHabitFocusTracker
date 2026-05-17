package com.example.todohabitfocus.feature.habit.presentation.add_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.core.domain.model.HabitFrequency
import com.example.todohabitfocus.core.ui.components.ModernTextField
import com.example.todohabitfocus.feature.habit.presentation.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditHabitScreen(
    onNavigateBack: () -> Unit,
    viewModel: HabitViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf(HabitFrequency.DAILY) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("New Habit", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            viewModel.addHabit(name, description, frequency)
                            onNavigateBack()
                        },
                        enabled = name.isNotBlank(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Create")
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
                Text("Habit Name", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                ModernTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "e.g. Drink Water"
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Frequency", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    HabitFrequency.entries.forEach { f ->
                        val isSelected = frequency == f
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(MaterialTheme.shapes.medium)
                                .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface)
                                .clickable { frequency = f }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                f.name,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Motivation", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                ModernTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = "Why do you want to build this habit?",
                    modifier = Modifier.height(100.dp)
                )
            }
        }
    }
}
