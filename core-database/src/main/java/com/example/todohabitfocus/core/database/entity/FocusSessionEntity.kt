package com.example.todohabitfocus.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todohabitfocus.core.domain.model.FocusSession
import com.example.todohabitfocus.core.domain.model.SessionType

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey val id: String,
    val startTime: Long,
    val durationMillis: Long,
    val type: SessionType,
    val taskId: String?
)

fun FocusSessionEntity.toExternalModel() = FocusSession(
    id = id,
    startTime = startTime,
    durationMillis = durationMillis,
    type = type,
    taskId = taskId
)

fun FocusSession.toEntity() = FocusSessionEntity(
    id = id,
    startTime = startTime,
    durationMillis = durationMillis,
    type = type,
    taskId = taskId
)
