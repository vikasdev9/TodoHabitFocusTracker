package com.example.todohabitfocus.core.domain.repository

import com.example.todohabitfocus.core.domain.model.FocusSession
import kotlinx.coroutines.flow.Flow

interface FocusRepository {
    fun getFocusSessions(): Flow<List<FocusSession>>
    suspend fun insertFocusSession(session: FocusSession)
    fun getTotalFocusTime(): Flow<Long>
}
