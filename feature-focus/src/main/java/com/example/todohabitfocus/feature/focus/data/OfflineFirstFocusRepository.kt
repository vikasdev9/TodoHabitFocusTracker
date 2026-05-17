package com.example.todohabitfocus.feature.focus.data

import com.example.todohabitfocus.core.database.dao.FocusDao
import com.example.todohabitfocus.core.database.entity.toEntity
import com.example.todohabitfocus.core.database.entity.toExternalModel
import com.example.todohabitfocus.core.domain.model.FocusSession
import com.example.todohabitfocus.core.domain.repository.FocusRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstFocusRepository @Inject constructor(
    private val focusDao: FocusDao
) : FocusRepository {
    override fun getFocusSessions(): Flow<List<FocusSession>> =
        focusDao.getFocusSessions().map { it.map { entity -> entity.toExternalModel() } }

    override suspend fun insertFocusSession(session: FocusSession) {
        focusDao.insertFocusSession(session.toEntity())
    }

    override fun getTotalFocusTime(): Flow<Long> =
        focusDao.getTotalFocusTime().map { it ?: 0L }
}
