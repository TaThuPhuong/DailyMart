package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.param.ChangePassParam
import net.fpoly.dailymart.data.model.param.LoginParam
import net.fpoly.dailymart.data.model.param.RegisterParam
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {
    @POST("register")
    fun register(@Body model: RegisterParam): Call<ResponseBody>

    @POST("login")
    fun login( @Body login: LoginParam): Call<ResponseBody>

    @POST("change-pass")
    fun changePassword(@Header("Authorization") token: String, @Body model: ChangePassParam): Call<ResponseBody>

    @POST("log-out")
    fun logout(@Header("Authorization") token: String)
}