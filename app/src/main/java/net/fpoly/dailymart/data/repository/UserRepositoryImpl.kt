package net.fpoly.dailymart.data.repository

import android.util.Log
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

    private val TAG = "YingMing"
    override suspend fun login(login: LoginParam): ResponseResult<User> =
        withContext(coroutineScope) {
            try {
                api.login(login)
            } catch (e: Exception) {
                Log.e(TAG, "login Exception: $e", )
                ResponseResult(0, "Máy chủ không phản hồi", null)
            }
        }

    override suspend fun getAllUser2(token: String): ResponseResult<List<UserRes>> =
        withContext(coroutineScope) {
            try {
                api.getAllUser2(token)
            } catch (e: Exception) {
                Log.e(TAG, "getAllUser2 Exception: $e", )
                ResponseResult(0, "Máy chủ không phản hồi", null)
            }
        }

//    override suspend fun updateUser(
//        token: String,
//        id: String,
//        userParam: UserRes
//    ): ResponseResult<Unit> = withContext(coroutineScope) {
//        try {
//            api.updateUser(token, id, userParam)
//        } catch (e: Exception) {
//            Log.e(TAG, "updateUser Exception: $e", )
//            ResponseResult(0, "Máy chủ không phản hồi", null)
//        }
//    }
}