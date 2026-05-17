package com.example.todohabitfocus.core.network.datasource

import com.example.todohabitfocus.core.network.model.RemoteTask
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import javax.inject.Inject

interface TaskRemoteDataSource {
    suspend fun getTasks(): List<RemoteTask>
    suspend fun upsertTask(task: RemoteTask)
    suspend fun deleteTask(id: String)
}

class SupabaseTaskRemoteDataSource @Inject constructor(
    private val supabaseClient: SupabaseClient
) : TaskRemoteDataSource {

    private val taskTable = supabaseClient.postgrest["tasks"]

    override suspend fun getTasks(): List<RemoteTask> {
        return taskTable.select {
            order("created_at", order = Order.DESCENDING)
        }.decodeList<RemoteTask>()
    }

    override suspend fun upsertTask(task: RemoteTask) {
        taskTable.upsert(task)
    }

    override suspend fun deleteTask(id: String) {
        taskTable.delete {
            filter {
                eq("id", id)
            }
        }
    }
}
