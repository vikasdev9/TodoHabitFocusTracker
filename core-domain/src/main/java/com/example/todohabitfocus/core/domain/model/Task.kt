package com.example.todohabitfocus.core.domain.model

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val dueDate: Long?,
    val priority: Priority,
    val isCompleted: Boolean,
    val category: String,
    val reminderTime: Long? = null,
    val isRecurring: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class Priority {
    LOW, MEDIUM, HIGH
}

enum class TaskStatus {
    PENDING, COMPLETED, OVERDUE
}
