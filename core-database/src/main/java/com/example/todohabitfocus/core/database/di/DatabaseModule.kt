package com.example.todohabitfocus.core.database.di

import android.content.Context
import androidx.room.Room
import com.example.todohabitfocus.core.database.TodoHabitFocusDatabase
import com.example.todohabitfocus.core.database.dao.HabitDao
import com.example.todohabitfocus.core.database.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoHabitFocusDatabase {
        return Room.databaseBuilder(
            context,
            TodoHabitFocusDatabase::class.java,
            "todo_habit_focus_db"
        ).build()
    }

    @Provides
    fun provideTaskDao(db: TodoHabitFocusDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideHabitDao(db: TodoHabitFocusDatabase): HabitDao = db.habitDao()
}
