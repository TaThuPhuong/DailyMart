package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.TaskParam
import net.fpoly.dailymart.data.model.TaskSuccess
import net.fpoly.dailymart.data.model.response.ResponseResult
import retrofit2.http.*

interface TaskApi {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("task")
    suspend fun insertTask(
        @Header("Authorization") token: String,
        @Body taskParam: TaskParam,
    ): ResponseResult<TaskSuccess>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("task/{id}")
    suspend fun updateTask(
        @Header("Authorization") token: String,
        @Body taskParam: TaskParam, @Path("id") id: String,
    ): ResponseResult<Unit>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @DELETE("task/{id}")
    suspend fun deleteTask(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): ResponseResult<Unit>

    @GET("task/detailTask/{id}")
    suspend fun getTaskById(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): ResponseResult<Task>

    @GET("task/getAll")
    suspend fun getAllTask(@Header("Authorization") token: String): ResponseResult<List<Task>>
}