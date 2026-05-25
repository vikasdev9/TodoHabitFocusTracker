package com.example.todohabitfocus.feature.task.presentation.add_edit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todohabitfocus.core.designsystem.theme.AppTheme
import com.example.todohabitfocus.core.domain.model.Priority
import com.example.todohabitfocus.feature.task.data.SubTask
import com.example.todohabitfocus.feature.task.data.TaskCategory
import com.example.todohabitfocus.feature.task.presentation.components.AddCategoryButton
import com.example.todohabitfocus.feature.task.presentation.components.CategoryChip
import com.example.todohabitfocus.feature.task.presentation.components.PeriodChip
import com.example.todohabitfocus.feature.task.presentation.components.PriorityButton
import com.example.todohabitfocus.feature.task.presentation.components.SectionTitle
import com.example.todohabitfocus.feature.task.utils.*
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    taskId: String? = null, onNavigateBack: () -> Unit
) {

    var taskName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var selectedPriority by remember {
        mutableStateOf(Priority.MEDIUM)
    }

    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    var reminderEnabled by remember {
        mutableStateOf(false)
    }

    var repeatType by remember {
        mutableStateOf(RepeatType.NONE)
    }

    var selectedCardColor by remember {
        mutableStateOf(softPurple)
    }

    val subTasks = remember {
        mutableStateListOf<SubTask>()
    }

    var subTaskText by remember {
        mutableStateOf("")
    }

    // Categories
    val categories = remember {
        mutableStateListOf(
            TaskCategory(
                "Work", primaryBlue, Icons.Default.Work
            ), TaskCategory(
                "Home", softGreen, Icons.Default.Home
            ), TaskCategory(
                "Purchase", softOrange, Icons.Default.ShoppingCart
            )
        )
    }

    var selectedCategory by remember {
        mutableStateOf(categories.first())
    }

    val sheetState = rememberModalBottomSheetState()

    var showCategorySheet by remember {
        mutableStateOf(false)
    }

    Scaffold(
        containerColor = backgroundColor, topBar = {

            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (taskId == null) "Create Task" else "Edit Task",
                        fontWeight = FontWeight.Bold
                    )
                }, navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.Close, contentDescription = "Back"
                        )
                    }
                }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundColor
                )
            )

        }) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            SectionTitle("Task Name")

            OutlinedTextField(
                value = taskName,
                onValueChange = {
                    taskName = it
                },
                placeholder = {
                    Text("What are you planning?")
                },
                leadingIcon = {
                    Icon(Icons.Default.Edit, null)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryBlue, unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle("Category")

            LazyRow {

                items(categories) { category ->

                    CategoryChip(
                        category = category, selected = selectedCategory == category
                    ) {
                        selectedCategory = category
                    }

                }

                item {

                    AddCategoryButton {
                        showCategorySheet = true
                    }

                }

            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle("Priority")

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                PriorityButton(
                    text = "LOW", selected = selectedPriority == Priority.LOW
                ) {
                    selectedPriority = Priority.LOW
                }

                PriorityButton(
                    text = "MEDIUM", selected = selectedPriority == Priority.MEDIUM
                ) {
                    selectedPriority = Priority.MEDIUM
                }

                PriorityButton(
                    text = "HIGH", selected = selectedPriority == Priority.HIGH
                ) {
                    selectedPriority = Priority.HIGH
                }

            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle("Sub Tasks")

            Row {

                OutlinedTextField(value = subTaskText, onValueChange = {
                    subTaskText = it
                }, modifier = Modifier.weight(1f), placeholder = {
                    Text("Add subtask")
                })

                Spacer(modifier = Modifier.width(10.dp))

                FloatingActionButton(
                    onClick = {

                        if (subTaskText.isNotEmpty()) {

                            subTasks.add(
                                SubTask(subTaskText)
                            )

                            subTaskText = ""
                        }

                    }, containerColor = primaryBlue
                ) {

                    Icon(
                        Icons.Default.Add, contentDescription = null, tint = Color.White
                    )

                }

            }

            Spacer(modifier = Modifier.height(12.dp))

            Column {

                subTasks.forEach { subTask ->

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {

                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Checkbox(
                                checked = subTask.completed, onCheckedChange = {})

                            Text(subTask.title)

                        }

                    }

                }

            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle("Period")

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                PeriodChip("Week")
                PeriodChip("Month")
                PeriodChip("Custom")

            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle("Due Date")

            ElevatedCard(
                shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.CalendarMonth, contentDescription = null, tint = primaryBlue
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Text(
                        selectedDate.toString(), fontSize = 16.sp
                    )

                }

            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle("Reminder")

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "Enable Notification Reminder", modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = reminderEnabled, onCheckedChange = {
                        reminderEnabled = it
                    })

            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle("Repeat")

            LazyRow {

                items(RepeatType.entries) { type ->

                    FilterChip(selected = repeatType == type, onClick = {
                        repeatType = type
                    }, label = {
                        Text(type.name)
                    })

                    Spacer(modifier = Modifier.width(8.dp))

                }

            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle("Audio Recording")

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)
            ) {

                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    FloatingActionButton(
                        onClick = {

                        }, containerColor = Color.Red
                    ) {

                        Icon(
                            Icons.Default.Mic, contentDescription = null, tint = Color.White
                        )

                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text("Tap to record voice note")

                }

            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle("Attachments")

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), shape = RoundedCornerShape(18.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Icon(
                        Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = primaryBlue,
                        modifier = Modifier.size(42.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Upload files or images")

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        "Camera / Gallery / Multiple", color = Color.Gray, fontSize = 12.sp
                    )

                }

            }

            Spacer(modifier = Modifier.height(22.dp))


            SectionTitle("Card Color")

            val colors = listOf(
                softPurple, softGreen, softOrange, Color(0xFFDCE8FF), Color(0xFFFFE2EC)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                colors.forEach { color ->

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (selectedCardColor == color) 3.dp else 0.dp,
                                color = primaryBlue,
                                shape = CircleShape
                            )
                            .clickable {
                                selectedCardColor = color
                            })

                }

            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle("Notes")

            OutlinedTextField(
                value = description, onValueChange = {
                    description = it
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), placeholder = {
                    Text("Add notes...")
                }, shape = RoundedCornerShape(18.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryBlue
                )
            ) {

                Text(
                    if (taskId == null) "Save Task" else "Update Task",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

        }

    }

    if (showCategorySheet) {

        ModalBottomSheet(
            onDismissRequest = {
                showCategorySheet = false
            }, sheetState = sheetState
        ) {

            var categoryName by remember {
                mutableStateOf("")
            }

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    "Create Custom Category", fontSize = 20.sp, fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(value = categoryName, onValueChange = {
                    categoryName = it
                }, modifier = Modifier.fillMaxWidth(), label = {
                    Text("Category Name")
                })

                Spacer(modifier = Modifier.height(20.dp))

                Text("Pick Color")

                Spacer(modifier = Modifier.height(12.dp))

                Row {

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.Blue)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                    )

                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {

                        categories.add(
                            TaskCategory(
                                categoryName, primaryBlue, Icons.Default.Star
                            )
                        )

                        showCategorySheet = false

                    }, modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Create Category")

                }

                Spacer(modifier = Modifier.height(40.dp))

            }

        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFFF8F9FD,
    device = "id:pixel_7_pro"
)
@Composable
fun AddTaskPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF8F9FD)
        ) {
            AddEditTaskScreen(
                taskId = null,
                onNavigateBack = {}
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFFF8F9FD,
    device = "id:pixel_7_pro",
    name = "Edit Task"
)
@Composable
fun EditTaskPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF8F9FD)
        ) {
            AddEditTaskScreen(
                taskId = "123",
                onNavigateBack = {}
            )
        }
    }
}

