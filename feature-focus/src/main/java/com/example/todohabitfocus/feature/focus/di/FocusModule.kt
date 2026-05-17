package com.example.todohabitfocus.feature.focus.di

import com.example.todohabitfocus.core.database.TodoHabitFocusDatabase
import com.example.todohabitfocus.core.database.dao.FocusDao
import com.example.todohabitfocus.core.domain.repository.FocusRepository
import com.example.todohabitfocus.feature.focus.data.OfflineFirstFocusRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FocusModule {
    @Binds
    abstract fun bindFocusRepository(
        focusRepository: OfflineFirstFocusRepository
    ): FocusRepository

    companion object {
        @Provides
        @Singleton
        fun provideFocusDao(database: TodoHabitFocusDatabase): FocusDao =
            database.focusDao()
    }
}
