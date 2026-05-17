package com.example.todohabitfocus.feature.home.presentation

import com.example.todohabitfocus.core.domain.model.Habit
import com.example.todohabitfocus.core.domain.model.Task
import com.example.todohabitfocus.core.domain.model.FocusSession

data class HomeUiState(
    val userName: String = "User",
    val isLoading: Boolean = false,
    val upcomingTasks: List<Task> = emptyList(),
    val habitStreaks: List<HabitStreak> = emptyList(),
    val focusStats: FocusStats = FocusStats(),
    val productivityScore: Int = 0,
    val weeklyActivity: List<ActivityData> = emptyList(),
    val errorMessage: String? = null
)

data class HabitStreak(
    val habit: Habit,
    val streakCount: Int,
    val isCompletedToday: Boolean
)

data class FocusStats(
    val totalFocusTimeMinutes: Long = 0,
    val sessionsCompleted: Int = 0,
    val dailyGoalProgress: Float = 0f
)

data class ActivityData(
    val dayName: String,
    val taskCount: Int,
    val focusHours: Float
)
