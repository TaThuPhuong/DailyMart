package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.TaskParam
import net.fpoly.dailymart.data.model.TaskSuccess
import net.fpoly.dailymart.data.model.response.ResponseResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskApi {
    @POST("task")
    suspend fun insertTask(
        @Header("Authorization") token: String,
        @Body taskParam: TaskParam,
    ): ResponseResult<TaskSuccess>

    @PUT("task/{id}")
    suspend fun updateTask(
        @Header("Authorization") token: String,
        @Body taskParam: TaskParam, @Path("id") id: String,
    ): ResponseResult<Unit>

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

    @GET("task")
    suspend fun getAllTask(@Header("Authorization") token: String): ResponseResult<List<Task>>
}