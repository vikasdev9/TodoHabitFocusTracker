package com.example.todohabitfocus.feature.task.di

import com.example.todohabitfocus.core.domain.repository.TaskRepository
import com.example.todohabitfocus.feature.task.data.OfflineFirstTaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TaskModule {
    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepository: OfflineFirstTaskRepository
    ): TaskRepository
}
