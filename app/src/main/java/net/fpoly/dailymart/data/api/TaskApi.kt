package net.fpoly.dailymart.data.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TaskApi {
    @POST("createTask")
    fun insertTask(
        @Header("Authorization") token: String,
        @Body taskParam: TaskParam,
    ): Call<ResponseBody>

    @PUT("updateTask")
    fun updateTask(
        @Header("Authorization") token: String,
        @Body taskParam: TaskParam, @Query("_id") id: String,
    ): Call<ResponseBody>

    @DELETE("deleteTask")
    fun deleteTask(
        @Header("Authorization") token: String,
        @Query("_id") id: String,
    ): Call<ResponseBody>

    @GET("task")
    fun getTaskById(
        @Header("Authorization") token: String,
        @Query("_id") id: String,
    ): Call<ResponseBody>

    @GET("getAllTask")
    fun getAllTask(@Header("Authorization") token: String): Call<ResponseBody>
}