package com.example.todohabitfocus.core.preferences.di

import com.example.todohabitfocus.core.preferences.data.PreferencesRepositoryImpl
import com.example.todohabitfocus.core.preferences.domain.PreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        preferencesRepositoryImpl: PreferencesRepositoryImpl
    ): PreferencesRepository
}
