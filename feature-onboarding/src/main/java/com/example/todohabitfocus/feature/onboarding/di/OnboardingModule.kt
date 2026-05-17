package com.example.todohabitfocus.feature.onboarding.di

import com.example.todohabitfocus.feature.onboarding.data.DataStoreOnboardingRepository
import com.example.todohabitfocus.feature.onboarding.domain.OnboardingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OnboardingModule {

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(
        onboardingRepositoryImpl: DataStoreOnboardingRepository
    ): OnboardingRepository
}
