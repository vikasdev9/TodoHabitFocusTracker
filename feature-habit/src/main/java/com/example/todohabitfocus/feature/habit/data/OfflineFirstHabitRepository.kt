package com.example.todohabitfocus.feature.habit.data

import com.example.todohabitfocus.core.database.dao.HabitDao
import com.example.todohabitfocus.core.database.entity.toEntity
import com.example.todohabitfocus.core.database.entity.toExternalModel
import com.example.todohabitfocus.core.domain.model.Habit
import com.example.todohabitfocus.core.domain.model.HabitLog
import com.example.todohabitfocus.core.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstHabitRepository @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {
    override fun getHabits(): Flow<List<Habit>> =
        habitDao.getHabits().map { it.map { entity -> entity.toExternalModel() } }

    override suspend fun insertHabit(habit: Habit) {
        habitDao.upsertHabit(habit.toEntity())
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.upsertHabit(habit.toEntity())
    }

    override suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit.toEntity())
    }

    override fun getHabitLogs(habitId: String): Flow<List<HabitLog>> =
        habitDao.getHabitLogs(habitId).map { it.map { entity -> entity.toExternalModel() } }

    override suspend fun insertHabitLog(log: HabitLog) {
        habitDao.upsertHabitLog(log.toEntity())
    }
}
