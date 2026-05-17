package com.example.todohabitfocus.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todohabitfocus.core.domain.model.Habit
import com.example.todohabitfocus.core.domain.model.HabitFrequency
import com.example.todohabitfocus.core.domain.model.HabitLog

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val frequency: HabitFrequency,
    val reminderTime: String?,
    val createdAt: Long,
    val icon: String
)

@Entity(tableName = "habit_logs")
data class HabitLogEntity(
    @PrimaryKey val id: String,
    val habitId: String,
    val date: Long,
    val isCompleted: Boolean
)

fun HabitEntity.toExternalModel() = Habit(
    id = id,
    name = name,
    description = description,
    frequency = frequency,
    reminderTime = reminderTime,
    createdAt = createdAt,
    icon = icon
)

fun Habit.toEntity() = HabitEntity(
    id = id,
    name = name,
    description = description,
    frequency = frequency,
    reminderTime = reminderTime,
    createdAt = createdAt,
    icon = icon
)

fun HabitLogEntity.toExternalModel() = HabitLog(
    id = id,
    habitId = habitId,
    date = date,
    isCompleted = isCompleted
)

fun HabitLog.toEntity() = HabitLogEntity(
    id = id,
    habitId = habitId,
    date = date,
    isCompleted = isCompleted
)
