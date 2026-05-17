package com.example.todohabitfocus.feature.analytics.domain.usecase

import com.example.todohabitfocus.feature.analytics.domain.model.AnalyticsSummary
import com.example.todohabitfocus.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnalyticsSummaryUseCase @Inject constructor(
    private val repository: AnalyticsRepository
) {
    operator fun invoke(): Flow<AnalyticsSummary> {
        return repository.getAnalyticsSummary()
    }
}
