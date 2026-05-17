package com.example.todohabitfocus.feature.analytics.domain.repository

import com.example.todohabitfocus.feature.analytics.domain.model.AnalyticsSummary
import kotlinx.coroutines.flow.Flow

interface AnalyticsRepository {
    fun getAnalyticsSummary(): Flow<AnalyticsSummary>
}
