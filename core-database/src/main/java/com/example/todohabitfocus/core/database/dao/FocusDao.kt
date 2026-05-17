package com.example.todohabitfocus.core.database.dao

import androidx.room.*
import com.example.todohabitfocus.core.database.entity.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusDao {
    @Query("SELECT * FROM focus_sessions ORDER BY startTime DESC")
    fun getFocusSessions(): Flow<List<FocusSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFocusSession(session: FocusSessionEntity)

    @Query("SELECT SUM(durationMillis) FROM focus_sessions")
    fun getTotalFocusTime(): Flow<Long?>
}
