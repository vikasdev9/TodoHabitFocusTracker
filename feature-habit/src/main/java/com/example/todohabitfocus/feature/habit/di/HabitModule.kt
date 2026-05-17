package com.example.todohabitfocus.feature.habit.di

import com.example.todohabitfocus.core.domain.repository.HabitRepository
import com.example.todohabitfocus.feature.habit.data.OfflineFirstHabitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HabitModule {
    @Binds
    @Singleton
    abstract fun bindHabitRepository(
        habitRepository: OfflineFirstHabitRepository
    ): HabitRepository
}
