package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.data.model.param.LoginParam
import net.fpoly.dailymart.data.model.response.ResponseResult

interface UserRepository {
    suspend fun login(login: LoginParam): ResponseResult<User>
    suspend fun getAllUser2(token: String): ResponseResult<List<UserRes>>
    suspend fun updateUser(token: String, id: String, userParam: UserRes): ResponseResult<Unit>
}