package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.ResultData
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
    suspend fun register(
        @Body model: RegisterParam,
        @Header("Authorization") token: String,
    ): ResultData<RegisterParam>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("register")
    fun createUser(
        @Body model: RegisterParam,
        @Header("Authorization") token: String,
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("login")
    suspend fun login(@Body login: LoginParam): ResponseResult<User>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("getAllNameUser")
    fun getAllUser(@Header("Authorization") token: String): Call<UserModel>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("change-pass")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body model: ChangePassParam,
    ): ResultData<ChangePassParam>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("sendMail")
    suspend fun forgotPassword(
        @Body forgotPass: ForgotPass,
    ): ResultData<String>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("forgot-pass")
    suspend fun sendOTP(
        @Body params: SendOtpParam,
    ): ResultData<String>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("log-out")
    fun logout(@Header("Authorization") token: String)

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("getAll")
    suspend fun getAllUser2(@Header("Authorization") token: String): ResponseResult<List<UserRes>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("updateUser/{id}")
    suspend fun updateUser2(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body userParam: UpdateParam,
    ): ResultData<UpdateParam>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("updateUser/{id}")
    fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body userParam: UserRes,
    ): Call<ResponseBody>
}