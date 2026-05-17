package com.example.todohabitfocus.core.database.dao

import androidx.room.*
import com.example.todohabitfocus.core.database.entity.HabitEntity
import com.example.todohabitfocus.core.database.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits")
    fun getHabits(): Flow<List<HabitEntity>>

    @Upsert
    suspend fun upsertHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId")
    fun getHabitLogs(habitId: String): Flow<List<HabitLogEntity>>

    @Upsert
    suspend fun upsertHabitLog(log: HabitLogEntity)
}
