package com.example.todohabitfocus.feature.analytics.di

import com.example.todohabitfocus.feature.analytics.data.repository.AnalyticsRepositoryImpl
import com.example.todohabitfocus.feature.analytics.domain.repository.AnalyticsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindAnalyticsRepository(
        analyticsRepositoryImpl: AnalyticsRepositoryImpl
    ): AnalyticsRepository
}
