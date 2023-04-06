package net.fpoly.dailymart.view.login

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.data.model.param.LoginParam
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.repository.UserRepository
import net.fpoly.dailymart.security.AESUtils
import net.fpoly.dailymart.utils.SharedPref

class LoginViewModel(private val app: Application, private val repo: UserRepository) : ViewModel() {

    private val TAG = "YingMing"
    private val _passwordStatus = MutableLiveData(false)
    val passwordStatus: LiveData<Boolean> = _passwordStatus

    private val _loginParam = MutableLiveData(LoginParam())

    private val _validatePhone = MutableLiveData("")
    val validatePhone: LiveData<String> = _validatePhone

    private val _validatePassword = MutableLiveData("")
    val validatePassword: LiveData<String> = _validatePassword

    val loginSuccess = MutableLiveData(false)
    val message = MutableLiveData("")

    private lateinit var mLoadingDialog: LoadingDialog

    init {
        _validatePhone.value = ""
        _validatePassword.value = ""
    }

    fun initLoadDialog(context: Context) {
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
                    } else {
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
                res.data?.let {
                    it.deviceId = SharedPref.getTokenNotification(app)
                    it.accessToken = AESUtils.encrypt(it.accessToken)
                    SharedPref.setAccessToken(app, it.accessToken)
                    SharedPref.insertUser(app, it)
                    repo.updateUser(SharedPref.getAccessToken(app), it.id, UserRes(it))
                    loginSuccess.postValue(true)
                    Log.d(TAG, "UserRes: ${UserRes(it)}")
                }
                message.postValue(res.message)
            } else {
                message.postValue(res.message)
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