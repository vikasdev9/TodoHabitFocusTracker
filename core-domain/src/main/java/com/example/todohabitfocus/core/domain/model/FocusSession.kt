package com.example.todohabitfocus.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FocusSession(
    val id: String,
    val startTime: Long,
    val durationMillis: Long,
    val type: SessionType,
    val taskId: String? = null
)

enum class SessionType {
    POMODORO, SHORT_BREAK, LONG_BREAK
}
