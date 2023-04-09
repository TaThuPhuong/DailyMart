package net.fpoly.dailymart.data.repository

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.api.TaskApi
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.TaskParam
import net.fpoly.dailymart.data.model.TaskSuccess
import net.fpoly.dailymart.data.model.response.ResponseResult
import net.fpoly.dailymart.repository.TaskRepository

class TaskRepositoryImpl(
    private val api: TaskApi = ServerInstance.apiTask,
    private val coroutineScope: CoroutineDispatcher = Dispatchers.IO
) : TaskRepository {

    private val TAG = "YingMing"

    override suspend fun insertTask(
        token: String,
        taskParam: TaskParam
    ): ResponseResult<TaskSuccess> = withContext(coroutineScope) {
        try {
            api.insertTask(token, taskParam).apply {
                Log.e(TAG, "insertTask: ${this.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "insertTask Ex: $e")
            ResponseResult(0, "Đã xảy ra lỗi trong quá trình", null)
        }
    }

    override suspend fun updateTask(
        token: String,
        taskParam: TaskParam,
        id: String
    ): ResponseResult<Unit> =
        withContext(coroutineScope) {
            try {
                api.updateTask(token, taskParam, id).apply {
                    Log.e(TAG, "updateTask: ${this.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "updateTask Ex: $e")
                ResponseResult(0, "Cập nhập thất bại", Unit)
            }
        }


    override suspend fun deleteTask(token: String, id: String): ResponseResult<Unit> =
        withContext(coroutineScope) {
            try {
                api.deleteTask(token, id).apply {
                    Log.e(TAG, "deleteTask: ${this.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "deleteTask Ex: $e")
                ResponseResult(0, "Xóa thất bại", null)
            }
        }

    override suspend fun getTaskById(token: String, id: String): ResponseResult<Task> =
        withContext(coroutineScope) {
            try {
                api.getTaskById(token, id).apply {
                    Log.e(TAG, "getTaskById: ${this.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "getTaskById Ex: $e")
                ResponseResult(0, "Lỗi", null)
            }
        }

    override suspend fun getAllTask(token: String): ResponseResult<List<Task>> =
        withContext(coroutineScope) {
            try {
                api.getAllTask(token).apply {
                    Log.e(TAG, "getAllTask: ${this.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "getAllTask Ex: $e")
                ResponseResult(0, "Lỗi", null)
            }
        }
}