package com.example.todohabitfocus.core.domain.repository

import com.example.todohabitfocus.core.domain.model.Habit
import com.example.todohabitfocus.core.domain.model.HabitLog
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getHabits(): Flow<List<Habit>>
    suspend fun insertHabit(habit: Habit)
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habit: Habit)
    
    fun getHabitLogs(habitId: String): Flow<List<HabitLog>>
    suspend fun insertHabitLog(log: HabitLog)
}
