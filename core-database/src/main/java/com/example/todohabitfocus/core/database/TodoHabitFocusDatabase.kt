package com.example.todohabitfocus.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todohabitfocus.core.database.dao.FocusDao
import com.example.todohabitfocus.core.database.dao.HabitDao
import com.example.todohabitfocus.core.database.dao.TaskDao
import com.example.todohabitfocus.core.database.entity.FocusSessionEntity
import com.example.todohabitfocus.core.database.entity.HabitEntity
import com.example.todohabitfocus.core.database.entity.HabitLogEntity
import com.example.todohabitfocus.core.database.entity.TaskEntity
import com.example.todohabitfocus.core.database.util.Converters

@Database(
    entities = [
        TaskEntity::class,
        HabitEntity::class,
        HabitLogEntity::class,
        FocusSessionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TodoHabitFocusDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun habitDao(): HabitDao
    abstract fun focusDao(): FocusDao
}
