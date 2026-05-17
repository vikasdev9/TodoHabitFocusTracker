package com.example.todohabitfocus.feature.analytics.data.repository

import com.example.todohabitfocus.feature.analytics.domain.model.AnalyticsSummary
import com.example.todohabitfocus.feature.analytics.domain.model.HabitConsistency
import com.example.todohabitfocus.feature.analytics.domain.model.ProductivityPoint
import com.example.todohabitfocus.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor() : AnalyticsRepository {
    override fun getAnalyticsSummary(): Flow<AnalyticsSummary> = flow {
        // Simulating data fetching from local DB/Repositories
        delay(1000)
        emit(
            AnalyticsSummary(
                totalFocusHours = 24.5f,
                tasksCompleted = 42,
                avgProductivityScore = 85,
                weeklyTrends = listOf(
                    ProductivityPoint("Mon", 60f),
                    ProductivityPoint("Tue", 80f),
                    ProductivityPoint("Wed", 75f),
                    ProductivityPoint("Thu", 90f),
                    ProductivityPoint("Fri", 85f),
                    ProductivityPoint("Sat", 40f),
                    ProductivityPoint("Sun", 30f)
                ),
                habitConsistencies = listOf(
                    HabitConsistency("Morning Run", 0.9f, 0xFF4CAF50),
                    HabitConsistency("Read 30m", 0.75f, 0xFF2196F3),
                    HabitConsistency("Meditation", 0.6f, 0xFF9C27B0)
                ),
                focusHourTrends = listOf(
                    ProductivityPoint("Mon", 4f),
                    ProductivityPoint("Tue", 5.5f),
                    ProductivityPoint("Wed", 3f),
                    ProductivityPoint("Thu", 6f),
                    ProductivityPoint("Fri", 4.5f),
                    ProductivityPoint("Sat", 1f),
                    ProductivityPoint("Sun", 0.5f)
                )
            )
        )
    }
}
