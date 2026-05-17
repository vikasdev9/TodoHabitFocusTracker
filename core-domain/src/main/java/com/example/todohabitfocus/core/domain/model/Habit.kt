package com.example.todohabitfocus.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Habit(
    val id: String,
    val name: String,
    val description: String,
    val frequency: HabitFrequency,
    val reminderTime: String?, // HH:mm
    val createdAt: Long = System.currentTimeMillis(),
    val icon: String = "default_habit"
)

enum class HabitFrequency {
    DAILY, WEEKLY, CUSTOM
}

@Serializable
data class HabitLog(
    val id: String,
    val habitId: String,
    val date: Long, // Timestamp
    val isCompleted: Boolean
)
