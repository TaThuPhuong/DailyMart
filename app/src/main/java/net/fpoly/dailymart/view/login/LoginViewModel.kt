package net.fpoly.dailymart.view.login

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gun0912.tedimagepicker.util.ToastUtil
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.param.LoginParam
import net.fpoly.dailymart.data.model.root.LoginRoot
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.security.AESUtils
import net.fpoly.dailymart.utils.SharedPref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val app: Application) : ViewModel() {

    private val TAG = "YingMing"
    private val _passwordStatus = MutableLiveData(false)
    val passwordStatus: LiveData<Boolean> = _passwordStatus

    private val _loginParam = MutableLiveData(LoginParam())

    private val _validatePhone = MutableLiveData("")
    val validatePhone: LiveData<String> = _validatePhone

    private val _validatePassword = MutableLiveData("")
    val validatePassword: LiveData<String> = _validatePassword

    val loginSuccess = MutableLiveData(false)

    private lateinit var mLoadingDialog: LoadingDialog

    init {
        _validatePhone.value = ""
        _validatePassword.value = ""
    }
    fun initLoadDialog(context: Context){
        mLoadingDialog = LoadingDialog(context)
    }

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
                _validatePassword.value = event.value.blankException()
            }

            is LoginEvent.Login -> {
                _loginParam.value?.let {
                    if (it.checkValidate()) {
                        mLoadingDialog.showLoading()
                        login(it)
                        showToast(app, "Đăng nhập thành công")
                    } else {
                        loginSuccess.value = false
                        showToast(app, "Chưa nhập tài khoản hoặc mật khẩu")
                    }
                }
            }
        }
    }

    private fun login(loginParam: LoginParam) {
        val server = ServerInstance.apiUser
        server.login(loginParam).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                mLoadingDialog.hideLoading()
                if (response.isSuccessful) {
                    val type = object : TypeToken<LoginRoot>() {}.type
                    val res: LoginRoot = Gson().fromJson(response.body()?.string(), type)
                    res.data.deviceId = SharedPref.getTokenNotification(app)
                    res.data.accessToken = AESUtils.encrypt(res.data.accessToken)
                    SharedPref.setAccessToken(app, res.data.accessToken)
                    SharedPref.insertUser(app, res.data)
                    // update lại deviceId
                    loginSuccess.value = true
                } else {
                    loginSuccess.value = false
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                mLoadingDialog.hideLoading()
                loginSuccess.value = false
            }
        })
    }
}

sealed class LoginEvent {
    data class OnPhoneNumberChange(val value: String) : LoginEvent()
    data class OnPasswordChange(val value: String) : LoginEvent()
    object ShowPassword : LoginEvent()
    object Login : LoginEvent()
}