package com.example.todohabitfocus.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todohabitfocus.core.domain.model.Priority
import com.example.todohabitfocus.core.domain.model.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val dueDate: Long?,
    val priority: Priority,
    val isCompleted: Boolean,
    val category: String,
    val reminderTime: Long?,
    val isRecurring: Boolean,
    val createdAt: Long
)

fun TaskEntity.toExternalModel() = Task(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate,
    priority = priority,
    isCompleted = isCompleted,
    category = category,
    reminderTime = reminderTime,
    isRecurring = isRecurring,
    createdAt = createdAt
)

fun Task.toEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate,
    priority = priority,
    isCompleted = isCompleted,
    category = category,
    reminderTime = reminderTime,
    isRecurring = isRecurring,
    createdAt = createdAt
)
