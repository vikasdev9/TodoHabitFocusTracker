package com.example.todohabitfocus.core.network.di

import com.example.todohabitfocus.core.network.datasource.SupabaseTaskRemoteDataSource
import com.example.todohabitfocus.core.network.datasource.TaskRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindTaskRemoteDataSource(
        impl: SupabaseTaskRemoteDataSource
    ): TaskRemoteDataSource

    companion object {
        @Provides
        @Singleton
        fun provideSupabaseClient(): SupabaseClient {
            return createSupabaseClient(
                supabaseUrl = "https://your-project.supabase.co",
                supabaseKey = "your-anon-key"
            ) {
                install(Auth)
                install(Postgrest)
            }
        }
    }
}
