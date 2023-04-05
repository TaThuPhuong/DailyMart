package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.data.model.param.ChangePassParam
import net.fpoly.dailymart.data.model.param.LoginParam
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.data.model.param.UserModel
import net.fpoly.dailymart.data.model.response.ResponseResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("register")
    fun register(@Body model: RegisterParam): Call<ResponseBody>

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

    @PUT("updateUser/{id}")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body userParam: UserRes
    ): ResponseResult<Unit>
}