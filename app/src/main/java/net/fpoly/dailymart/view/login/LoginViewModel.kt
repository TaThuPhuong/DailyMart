package net.fpoly.dailymart.view.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.data.model.param.LoginParam
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.repository.UserRepository
import net.fpoly.dailymart.security.AESUtils
import net.fpoly.dailymart.utils.SharedPref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val app: Application, private val repo: UserRepository) : ViewModel() {

    private val TAG = "YingMing"
    private val _passwordStatus = MutableLiveData(false)
    val passwordStatus: LiveData<Boolean> = _passwordStatus

    private val _loginParam = MutableLiveData(LoginParam())

    private val _validatePhone = MutableLiveData<String>(null)
    val validatePhone: LiveData<String> = _validatePhone

    private val _validatePassword = MutableLiveData<String>(null)
    val validatePassword: LiveData<String> = _validatePassword

    val loginSuccess = MutableLiveData(false)
    val message = MutableLiveData<String>()

    private val mDeviceId = SharedPref.getTokenNotification(app)

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.ShowPassword -> {
                _passwordStatus.value = !_passwordStatus.value!!
            }
            is LoginEvent.OnPhoneNumberChange -> {
                _loginParam.value = _loginParam.value?.copy(
                    phoneNumber = event.value
                )
                _validatePhone.value = event.value.blankException()
            }
            is LoginEvent.OnPasswordChange -> {
                _loginParam.value = _loginParam.value?.copy(
                    password = event.value
                )
            }

            is LoginEvent.Login -> {
                _loginParam.value?.let {
                    if (it.checkValidate()) {
                        login(it)
                    } else {
                        _validatePhone.value = it.phoneNumber.blankException()
                        _validatePassword.value = it.password.blankException()
                        loginSuccess.value = false
                    }
                }
            }
        }
    }

    private fun login(loginParam: LoginParam) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.login(loginParam)
            if (res.isSuccess()) {
                var user = res.data
                user = user?.copy(
                    deviceId = mDeviceId,
                    accessToken = AESUtils.encrypt(user.accessToken)
                )
                SharedPref.setAccessToken(app, user!!.accessToken)
                SharedPref.insertUser(app, user)
                val token = SharedPref.getAccessToken(app)
                val api = ServerInstance.apiUser
                try {
                    api.updateUser(token, user.id, UserRes(user))
                        .enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>,
                            ) {
                                response.body()?.string()?.let {
                                    Log.e(TAG, "body: $it")
                                }
                                response.errorBody()?.string()?.let {
                                    Log.e(TAG, "errorBody: $it")
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                            }

                        })
                } catch (e: Exception) {

                }
                if (user.disable) {
                    loginSuccess.postValue(true)
                } else {
                    message.postValue("Tài khoản đã bị khóa. Hãy liên hệ với quản lý")
                }

                Log.e(TAG, "deviceId: ${user.deviceId}")
                Log.e(TAG, "user: $user")
                Log.e(TAG, "getTokenNotification: ${SharedPref.getTokenNotification(app)}")
                Log.e(TAG, "UserRes: ${UserRes(user)}")
                message.postValue(res.message!!)
            } else {
                message.postValue(res.message!!)
                loginSuccess.postValue(false)
            }
        }
    }
}

sealed class LoginEvent {
    data class OnPhoneNumberChange(val value: String) : LoginEvent()
    data class OnPasswordChange(val value: String) : LoginEvent()
    object ShowPassword : LoginEvent()
    object Login : LoginEvent()
}