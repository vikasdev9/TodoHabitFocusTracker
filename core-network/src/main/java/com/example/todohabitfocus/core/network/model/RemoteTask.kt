package com.example.todohabitfocus.core.network.model

import com.example.todohabitfocus.core.domain.model.Priority
import com.example.todohabitfocus.core.domain.model.Task
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteTask(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("due_date") val dueDate: Long? = null,
    @SerialName("priority") val priority: Priority,
    @SerialName("is_completed") val isCompleted: Boolean,
    @SerialName("category") val category: String,
    @SerialName("reminder_time") val reminderTime: Long? = null,
    @SerialName("is_recurring") val isRecurring: Boolean = false,
    @SerialName("created_at") val createdAt: Long
)

fun RemoteTask.toDomainModel() = Task(
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

fun Task.toRemoteModel() = RemoteTask(
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
