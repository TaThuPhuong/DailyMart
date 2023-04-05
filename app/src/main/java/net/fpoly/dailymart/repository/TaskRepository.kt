package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.data.model.response.ResponseResult

interface TaskRepository {
    suspend fun insertTask(token: String, taskParam: TaskParam): ResponseResult<TaskSuccess>

    suspend fun updateTask(token: String, taskParam: TaskParam, id: String): ResponseResult<Unit>

    suspend fun deleteTask(token: String, id: String): ResponseResult<Unit>

    suspend fun getTaskById(token: String, id: String): ResponseResult<Task>

    suspend fun getAllTask(token: String): ResponseResult<List<Task>>
}