package com.example.todohabitfocus.feature.analytics.domain.model

data class ProductivityPoint(
    val label: String,
    val value: Float
)

data class HabitConsistency(
    val habitName: String,
    val percentage: Float,
    val color: Long
)

data class AnalyticsSummary(
    val totalFocusHours: Float,
    val tasksCompleted: Int,
    val avgProductivityScore: Int,
    val weeklyTrends: List<ProductivityPoint>,
    val habitConsistencies: List<HabitConsistency>,
    val focusHourTrends: List<ProductivityPoint>
)
