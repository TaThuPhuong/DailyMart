package net.fpoly.dailymart.view.reset_password

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.param.ChangePassParam
import net.fpoly.dailymart.data.model.param.SendOtpParam
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.change_password.ChangePasswordActivity
import net.fpoly.dailymart.view.login.LoginActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class ResetPasswordViewModel(app: Application) : ViewModel() {
    private val TAG = "Tuvm"
    private val mToken = SharedPref.getAccessToken(app)
    private val server = ServerInstance.apiUser

    private val _validateOldPass = MutableLiveData("")
    val validateOldPass: LiveData<String> = _validateOldPass
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
        server.sendOTP(
            sendOtpParam
        )
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        if (response.message() == "Update new password success!")
                            Toast.makeText(
                                context,
                                response.message().toString(),
                                Toast.LENGTH_LONG
                            )
                                .show()
                        Log.d(TAG, "Response : " + response.body().toString())
                        Log.e(TAG, "code: " + response.code())
                        Log.e(TAG, "message: " + response.message())
                        Log.e(TAG, "errorBody: " + response.errorBody()?.string())
                        mLoadingDialog.hideLoading()
                        activity?.startActivity(Intent(context, LoginActivity::class.java))
                    } else {
                        Log.e(TAG, "errorBody: " + response.errorBody())
                        mLoadingDialog.hideLoading()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e(TAG, "Response : " + t.message)
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    mLoadingDialog.hideLoading()
                }
            })
    }

    fun onEvent(event: SetupEvent, context: Context) {
        when (event) {
            is SetupEvent.OnOTP -> {
                _changeParam.value = _changeParam.value?.copy(
                    token = event.value
                )
                _validateOldPass.value = event.value.blankException()
            }
            is SetupEvent.OnNewPass -> {
                _changeParam.value = _changeParam.value?.copy(
                    newPassword = event.value
                )
                _validateNewPass.value = event.value.blankException()
            }
            is SetupEvent.OnConfirm -> {
                _validateConfirm.value = event.value.blankException()
            }

            is SetupEvent.ValidateForm -> {
                _changeParam.value?.let {
                    if (it.checkValidate()) {
                        mLoadingDialog.showLoading()
                        sendOTP(
                            sendOtpParam = it,
                            context = context,
                            activity = null
                        );
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
}