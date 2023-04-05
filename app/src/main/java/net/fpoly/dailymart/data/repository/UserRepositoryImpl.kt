package net.fpoly.dailymart.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.api.UserApi
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.data.model.param.LoginParam
import net.fpoly.dailymart.data.model.response.ResponseResult
import net.fpoly.dailymart.repository.UserRepository

class UserRepositoryImpl(
    private val api: UserApi = ServerInstance.apiUser,
    private val coroutineScope: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {
    override suspend fun login(login: LoginParam): ResponseResult<User> =
        withContext(coroutineScope) {
            try {
                api.login(login)
            } catch (e: Exception) {
                ResponseResult(0, "Đã xảy ra lỗi trong quá trình đăng nhập", null)
            }
        }

    override suspend fun getAllUser2(token: String): ResponseResult<List<UserRes>> =
        withContext(coroutineScope) {
            try {
                api.getAllUser2(token)
            } catch (e: Exception) {
                ResponseResult(0, "Đã xảy ra lỗi", null)
            }
        }

    override suspend fun updateUser(
        token: String,
        id: String,
        userParam: UserRes
    ): ResponseResult<Unit> = withContext(coroutineScope){
        try {
            api.updateUser(token,id,userParam)
        } catch (e: Exception) {
            ResponseResult(0, "Đã xảy ra lỗi", null)
        }
    }
}