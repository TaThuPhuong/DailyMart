package net.fpoly.dailymart.data.repository

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.api.UserApi
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.data.model.param.*
import net.fpoly.dailymart.data.model.response.ResponseResult
import net.fpoly.dailymart.repository.UserRepository

class UserRepositoryImpl(
    private val api: UserApi = ServerInstance.apiUser,
    private val coroutineScope: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    private val TAG = "YingMing"
    private val TAGS = "Tuvm"
    override suspend fun login(login: LoginParam): ResponseResult<User> =
        withContext(coroutineScope) {
            try {
                api.login(login)
            } catch (e: Exception) {
                Log.e(TAG, "login Exception: $e")
                ResponseResult(0, "Đã xảy ra lỗi trong quá trình đăng nhập", null)
            }
        }

    override suspend fun getAllUser2(token: String): ResponseResult<List<UserRes>> =
        withContext(coroutineScope) {
            try {
                api.getAllUser2(token)
            } catch (e: Exception) {
                Log.e(TAG, "getAllUser2 Exception: $e")
                ResponseResult(0, "Máy chủ không phản hồi", null)
            }
        }

    override suspend fun createUser(token: String, userParam: RegisterParam) =
        withContext(coroutineScope) {
            try {
                Log.e(TAGS, "createUser: ${Gson().toJson(userParam)}")
                val res = api.register(token = token, model = userParam)
                if (res.isSuccess()) {
                    Response.Success(res.result, res.message)
                } else {
                    Response.Error(res.message)
                }

            } catch (e: Exception) {
                Log.e(TAGS, "createUser Exception: $e")
                Response.Error(e.message.toString())
            }
        }

    override suspend fun updateUser(
        token: String,
        id: String,
        updateParam: UpdateParam
    ) =
        withContext(coroutineScope) {
            try {
                Log.e(TAGS, "update user: ${Gson().toJson(updateParam)}")
                val res = api.updateUser2(token = token, id = id, userParam = updateParam)
                if (res.isSuccess()) {
                    Response.Success(res.result, res.message)
                } else {
                    Response.Error(res.message)
                }
            } catch (e: Exception) {
                Log.e(TAGS, "updateUser Exception: $e")
                Response.Error(e.message.toString())
            }
        }

    override suspend fun changePass(
        token: String,
        changePassParam: ChangePassParam,
    ) =
        withContext(coroutineScope) {
            try {
                Log.e(TAGS, "changePass: ${Gson().toJson(changePassParam)}")
                val res = api.changePassword(token = token, changePassParam)
                if (res.isSuccess()) {
                    Response.Success(res.result, res.message)
                } else {
                    Response.Error(res.message)
                }
            } catch (e: Exception) {
                Log.e(TAGS, "change pass Exception: $e")
                Response.Error(e.message.toString())
            }
        }

    override suspend fun forgotPass(forgotPass: ForgotPass) =
        withContext(coroutineScope) {
            try {
                Log.e(TAGS, "forgot pass: ${Gson().toJson(forgotPass)}")
                val res = api.forgotPassword(forgotPass)
                if (res.isSuccess()) {
                    Response.Success(res.result, res.message)
//                    Log.e(TAGS, "Forgot pass viewModel: " + res.result)
                } else {
                    Response.Error(res.message)
                }
            } catch (e: Exception) {
                Log.e(TAGS, "forgot pass Exception: $e")
                Response.Error(e.message.toString())
            }
        }
}