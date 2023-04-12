package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.data.model.param.*
import net.fpoly.dailymart.data.model.response.ResponseResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("register")
    fun register(
        @Body model: RegisterParam,
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("login")
    suspend fun login(@Body login: LoginParam): ResponseResult<User>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("getAllNameUser")
    fun getAllUser(@Header("Authorization") token: String): Call<UserModel>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("change-pass")
    fun changePassword(
        @Header("Authorization") token: String,
        @Body model: ChangePassParam,
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("log-out")
    fun logout(@Header("Authorization") token: String)

    @GET("getAllNameUser")
    suspend fun getAllUser2(@Header("Authorization") token: String): ResponseResult<List<UserRes>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("updateUser/{id}")
    fun updateUser2(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body userParam: RegisterParam
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("updateUser/{id}")
    fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body userParam: UserRes
    ): ResponseResult<Unit>
}