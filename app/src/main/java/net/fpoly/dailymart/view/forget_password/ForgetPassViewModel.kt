package net.fpoly.dailymart.view.forget_password

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
import net.fpoly.dailymart.data.model.param.ForgotPass
import net.fpoly.dailymart.data.repository.UserRepositoryImpl
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.view.reset_password.ResetPasswordActivity
import net.fpoly.dailymart.data.model.Response.Success
import net.fpoly.dailymart.data.model.Response.Error

class ForgetPassViewModel : ViewModel() {
    private val TAG = "Tuvm"
    private val forgotPassRepo = UserRepositoryImpl()
    private val _validateEmail = MutableLiveData("")
    val validateEmail: LiveData<String> = _validateEmail
    private val _forgotPass = MutableLiveData(ForgotPass())
    private lateinit var mLoadingDialog: LoadingDialog
    var resSend = String()

    fun initLoadDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context)
    }

    init {
        _validateEmail.value = ""
    }

    fun sendOTP(
        forgotPass: ForgotPass,
        context: Context,
        activity: ForgetPasswordActivity?
    ) {

        Log.d(TAG, "Params : $forgotPass")
        mLoadingDialog.showLoading()
        viewModelScope.launch {
            when (val forgot = forgotPassRepo.forgotPass(forgotPass)) {
                is Success -> {
                    resSend = forgot.message
                    Toast.makeText(context, forgot.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, ResetPasswordActivity::class.java)
                    intent.putExtra("id", resSend)
                    Log.e(TAG, "sendOTP: $resSend", )
                    activity?.startActivity(intent)
                }
                is Error -> {
                    _validateEmail.value = forgot.message
                    mLoadingDialog.hideLoading()
                }
            }
        }

    }

    fun onEvent(event: ForgotEvent, context: Context) {
        when (event) {
            is ForgotEvent.OnEmail -> {
                _forgotPass.value = _forgotPass.value?.copy(
                    mail = event.value
                )
                if (event.value.isEmpty()) {
                    _validateEmail.value = event.value.blankException()
                } else {
                    _validateEmail.value = ""
                }
            }

            is ForgotEvent.ValidateForm -> {
                _forgotPass.value?.let {
                    if (it.checkValidate()) {
//                        sendOTP(
//                            forgotPass = it,
//                            context = context,
//                            activity = null
//                        )
                    } else {
                        mLoadingDialog.hideLoading()
                    }
                }
            }
        }
    }

    sealed class ForgotEvent {
        data class OnEmail(val value: String) : ForgotEvent()
        object ValidateForm : ForgotEvent()
    }
}