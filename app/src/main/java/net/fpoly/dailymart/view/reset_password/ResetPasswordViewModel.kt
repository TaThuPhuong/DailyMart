package net.fpoly.dailymart.view.reset_password

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.param.SendOtpParam
import net.fpoly.dailymart.data.repository.UserRepositoryImpl
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.data.model.Response.Success
import net.fpoly.dailymart.data.model.Response.Error
import net.fpoly.dailymart.view.login.LoginActivity

class ResetPasswordViewModel(app: Application) : ViewModel() {
    private val TAG = "Tuvm"
    private val mToken = SharedPref.getAccessToken(app)

    private val forgotPassRepo = UserRepositoryImpl()
    private val _validateOtp = MutableLiveData("")
    val validateOtp: LiveData<String> = _validateOtp
    private val _validateNewPass = MutableLiveData("")
    val validateNewPass: LiveData<String> = _validateNewPass
    private val _validateConfirm = MutableLiveData("")
    val validateConfirm: LiveData<String> = _validateConfirm
    private val _changeParam = MutableLiveData(SendOtpParam())
    private lateinit var mLoadingDialog: LoadingDialog

    fun initLoadDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context)
    }

    fun sendOTP(
        sendOtpParam: SendOtpParam,
        context: Context,
        activity: ResetPasswordActivity?
    ) {
        Log.d(TAG, "Params : $sendOtpParam")
        Log.d(TAG, "token : $mToken")
        mLoadingDialog.showLoading()

        viewModelScope.launch {
            when (val resetPass = forgotPassRepo.resetPass(sendOtpParam)) {
                is Success -> {
                    Log.e(TAG, "sendOTP: ${resetPass.data}")
                    Toast.makeText(context, resetPass.message, Toast.LENGTH_SHORT).show()
                    activity?.startActivity(Intent(context, LoginActivity::class.java))
                    mLoadingDialog.hideLoading()
                }

                is Error -> {
                    _validateOtp.value = resetPass.message
                    mLoadingDialog.hideLoading()
                }
            }
        }
    }

    fun onEvent(event: SetupEvent, context: Context) {
        when (event) {
            is SetupEvent.OnOTP -> {
                _changeParam.value = _changeParam.value?.copy(
                    token = event.value
                )
                if (event.value.trim().isEmpty()) {
                    _validateOtp.value = event.value.blankException()
                } else {
                    _validateOtp.value = ""
                }
            }

            is SetupEvent.OnNewPass -> {
                _changeParam.value = _changeParam.value?.copy(
                    newPassword = event.value
                )
                if (event.value.isEmpty()) {
                    _validateNewPass.value = event.value.blankException()
                } else if (!validatePassword(event.value)) {
                    _validateNewPass.value =
                        "Mật khẩu tối thiểu 8 kí tự có chữ hoa chữ thường, số và kí tự đặc biệt"
                } else {
                    _validateNewPass.value = ""
                }
            }

            is SetupEvent.OnConfirm -> {
                if (event.value.isEmpty()) {
                    _validateConfirm.value = event.value.blankException()
                } else if (event.value != _changeParam.value?.newPassword) {
                    _validateConfirm.value = "Mật khẩu không khớp !"
                } else {
                    _validateConfirm.value = ""
                }
            }

            is SetupEvent.ValidateForm -> {
                _changeParam.value?.let {
                    if (it.checkValidate()) {
                        mLoadingDialog.showLoading()
                        sendOTP(
                            sendOtpParam = it,
                            context = context,
                            activity = null
                        )
                    } else {
                        mLoadingDialog.hideLoading()
                    }
                }
            }
        }
    }

    sealed class SetupEvent {
        data class OnOTP(val value: String) : SetupEvent()
        data class OnNewPass(val value: String) : SetupEvent()
        data class OnConfirm(val value: String) : SetupEvent()
        object ValidateForm : SetupEvent()
    }

    private fun validatePassword(password: String): Boolean {
        val pattern =
            "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[,.!@#\\/\\$&*~\"|:]).{8,}".toRegex()
        return pattern.matches(password)
    }
}