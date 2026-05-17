package com.example.todohabitfocus.feature.task.data

import android.util.Log
import com.example.todohabitfocus.core.database.dao.TaskDao
import com.example.todohabitfocus.core.database.entity.toEntity
import com.example.todohabitfocus.core.database.entity.toExternalModel
import com.example.todohabitfocus.core.domain.model.Task
import com.example.todohabitfocus.core.domain.repository.TaskRepository
import com.example.todohabitfocus.core.network.datasource.TaskRemoteDataSource
import com.example.todohabitfocus.core.network.model.toDomainModel
import com.example.todohabitfocus.core.network.model.toRemoteModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstTaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val remoteDataSource: TaskRemoteDataSource
) : TaskRepository {

    override fun getTasks(): Flow<List<Task>> =
        taskDao.getTasks().map { it.map { entity -> entity.toExternalModel() } }

    override suspend fun getTaskById(id: String): Task? =
        taskDao.getTaskById(id)?.toExternalModel()

    override suspend fun insertTask(task: Task) {
        // Local first
        taskDao.upsertTask(task.toEntity())
        
        // Try sync to remote
        try {
            remoteDataSource.upsertTask(task.toRemoteModel())
        } catch (e: Exception) {
            Log.e("TaskRepository", "Failed to sync inserted task to remote: ${task.id}", e)
        }
    }

    override suspend fun updateTask(task: Task) {
        // Local first
        taskDao.upsertTask(task.toEntity())
        
        // Try sync to remote
        try {
            remoteDataSource.upsertTask(task.toRemoteModel())
        } catch (e: Exception) {
            Log.e("TaskRepository", "Failed to sync updated task to remote: ${task.id}", e)
        }
    }

    override suspend fun deleteTask(task: Task) {
        // Local first
        taskDao.deleteTask(task.toEntity())
        
        // Try sync to remote
        try {
            remoteDataSource.deleteTask(task.id)
        } catch (e: Exception) {
            Log.e("TaskRepository", "Failed to sync deleted task to remote: ${task.id}", e)
        }
    }

    override suspend fun syncTasks() {
        try {
            // Simple sync logic: Remote is source of truth
            val remoteTasks = remoteDataSource.getTasks().map { it.toDomainModel() }
            
            // Upsert all remote tasks into local database
            taskDao.upsertTasks(remoteTasks.map { it.toEntity() })
            
            Log.d("TaskRepository", "Sync completed successfully")
        } catch (e: Exception) {
            Log.e("TaskRepository", "Sync failed", e)
            throw e
        }
    }
}
