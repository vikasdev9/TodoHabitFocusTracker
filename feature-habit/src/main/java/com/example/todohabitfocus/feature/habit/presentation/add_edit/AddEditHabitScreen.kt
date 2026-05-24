package com.example.todohabitfocus.feature.habit.presentation.add_edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todohabitfocus.core.domain.model.HabitFrequency
import com.example.todohabitfocus.feature.habit.presentation.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditHabitScreen(
    onNavigateBack: () -> Unit,
    viewModel: HabitViewModel = hiltViewModel()
) {

    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var frequency by rememberSaveable { mutableStateOf(HabitFrequency.DAILY) }

    val colors = remember {
        listOf(
            Color(0xFFDDF3FF),
            Color(0xFFE8DDF8),
            Color(0xFFF9DDCF),
            Color(0xFFD8F3E4),
            Color(0xFFF6E9B9),
            Color(0xFFF8D9DF)
        )
    }

    val icons = remember {
        listOf(
            Icons.Default.SelfImprovement,
            Icons.Default.FitnessCenter,
            Icons.Default.MenuBook,
            Icons.Default.DirectionsBike,
            Icons.Default.Air,
            Icons.Default.ShowChart,
            Icons.Default.LocalFireDepartment,
            Icons.Default.Bolt
        )
    }

    var selectedColor by rememberSaveable { mutableIntStateOf(1) }
    var selectedIcon by rememberSaveable { mutableIntStateOf(7) }
    var selectedTracking by rememberSaveable { mutableIntStateOf(0) }

    val weekDays = remember {
        listOf("M", "T", "W", "T", "F", "S", "S")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF7F8FC),
        topBar = {},
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .navigationBarsPadding()
                .padding(horizontal = 22.dp),
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            // TOP HANDLE
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD8DCE6))
            )

            Spacer(modifier = Modifier.height(20.dp))

            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                Column {

                    Text(
                        text = "NEW HABIT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7A7FE2),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Build a better day",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Color(0xFF081225)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable {
                            onNavigateBack()
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // PREVIEW CARD
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colors[selectedColor]
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = icons[selectedIcon],
                            contentDescription = null,
                            tint = Color(0xFF6C4AB6)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = if (name.isBlank()) "Your habit name" else name,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6C4AB6)
                        )

                        Text(
                            text = "Every day · 08:00",
                            fontSize = 12.sp,
                            color = Color(0xFF8B7CF6)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.6f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFF8B7CF6),
                                modifier = Modifier.size(12.dp)
                            )

                            Spacer(modifier = Modifier.width(2.dp))

                            Text(
                                text = "0",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8B7CF6)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // HABIT NAME
            FormLabel("HABIT NAME")

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "Drink water, Meditate...",
                        color = Color(0xFF9AA3B2)
                    )
                },
                shape = RoundedCornerShape(22.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFFE3E7EF),
                    unfocusedBorderColor = Color(0xFFE3E7EF)
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // DESCRIPTION
            FormLabel("DESCRIPTION")

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(92.dp),
                placeholder = {
                    Text(
                        "Why does this matter to you?",
                        color = Color(0xFF9AA3B2)
                    )
                },
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFFE3E7EF),
                    unfocusedBorderColor = Color(0xFFE3E7EF)
                )
            )

            Spacer(modifier = Modifier.height(22.dp))

            // COLOR
            FormLabel("COLOR")

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                colors.forEachIndexed { index, color ->

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (selectedColor == index) 2.dp else 0.dp,
                                color = Color(0xFF8B7CF6),
                                shape = CircleShape
                            )
                            .clickable {
                                selectedColor = index
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ICON
            FormLabel("ICON")

            Spacer(modifier = Modifier.height(14.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier.height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false
            ) {

                itemsIndexed(
                    items = icons,
                    key = { index, _ -> index }
                ) { index, icon ->

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(
                                width = if (selectedIcon == index) 2.dp else 1.dp,
                                color = if (selectedIcon == index)
                                    Color(0xFF8B7CF6)
                                else
                                    Color(0xFFE5E7EB),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                selectedIcon = index
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color(0xFF081225),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // FREQUENCY
            FormLabel("FREQUENCY")

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                HabitFrequency.entries.forEachIndexed { index, item ->

                    val selected = frequency == item

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (selected)
                                    Color(0xFF3554D1)
                                else
                                    Color.White
                            )
                            .clickable {
                                frequency = item
                            }
                            .padding(horizontal = 28.dp, vertical = 12.dp)
                    ) {

                        Text(
                            text = item.name.lowercase()
                                .replaceFirstChar { it.uppercase() },
                            fontWeight = FontWeight.Bold,
                            color = if (selected)
                                Color.White
                            else
                                Color(0xFF6B7280),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // DAYS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                weekDays.forEach { day ->

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3554D1)),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = day,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // TIME + TARGET
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                SmallInputCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Notifications,
                    value = "08:00",
                    suffix = ""
                )

                SmallInputCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.TrackChanges,
                    value = "7",
                    suffix = "days"
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // TRACKING
            FormLabel("TRACKING")

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                val tracking = listOf(
                    "Check",
                    "Count",
                    "Timer"
                )

                tracking.forEachIndexed { index, title ->

                    val selected = selectedTracking == index

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (selected)
                                    Color(0xFF3554D1)
                                else
                                    Color.White
                            )
                            .clickable {
                                selectedTracking = index
                            }
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = when (index) {
                                    0 -> Icons.Default.Check
                                    1 -> Icons.Default.Add
                                    else -> Icons.Default.Timer
                                },
                                contentDescription = null,
                                tint = if (selected)
                                    Color.White
                                else
                                    Color(0xFF6B7280),
                                modifier = Modifier.size(14.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                color = if (selected)
                                    Color.White
                                else
                                    Color(0xFF6B7280),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // BUTTONS
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Button(
                    onClick = {
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {

                    Text(
                        text = "Cancel",
                        color = Color(0xFF081225),
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        viewModel.addHabit(
                            name = name,
                            description = description,
                            frequency = frequency
                        )
                        onNavigateBack()
                    },
                    enabled = name.isNotBlank(),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3554D1),
                        disabledContainerColor = Color(0xFFB8C2F2)
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {

                    Text(
                        text = "Create Habit",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun FormLabel(
    text: String
) {

    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF6B7280),
        letterSpacing = 1.sp
    )
}

@Composable
fun SmallInputCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    suffix: String
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFFE5E7EB)
        )
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 14.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF3554D1),
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF081225)
            )

            Spacer(modifier = Modifier.weight(1f))

            if (suffix.isNotEmpty()) {

                Text(
                    text = suffix,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}
