package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.data.model.param.*
import net.fpoly.dailymart.data.model.response.ResponseResult

interface UserRepository {
    suspend fun login(login: LoginParam): ResponseResult<User>
    suspend fun getAllUser2(token: String): ResponseResult<List<UserRes>>
    suspend fun createUser(token: String, userParam: RegisterParam): Response<RegisterParam>
    suspend fun updateUser(
        token: String,
        id: String,
        updateParam: UpdateParam,
    ): Response<UpdateParam>


    suspend fun changePass(
        token: String,
        changePassParam: ChangePassParam,
    ): Response<ChangePassParam>

    suspend fun forgotPass(
        forgotPass: ForgotPass,
    ): Response<String>

    suspend fun resetPass(
        sendOtpParam: SendOtpParam,
    ): Response<String>

    suspend fun getUserById(token: String, id: String): Response<UserRes>
}