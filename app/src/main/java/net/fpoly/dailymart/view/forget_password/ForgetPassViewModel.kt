package net.fpoly.dailymart.view.forget_password

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.param.ForgotPass
import net.fpoly.dailymart.data.model.response.ResponseResult
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.utils.SharedPref
import okhttp3.Call
import okhttp3.Response
import okhttp3.ResponseBody

class ForgetPassViewModel(app: Application) : ViewModel() {
    private val TAG = "Tuvm"
    private val mToken = SharedPref.getAccessToken(app)
    private val server = ServerInstance.apiUser

    private val _validateOldPass = MutableLiveData("")
    val validateOldPass: LiveData<String> = _validateOldPass
    private val _forgotPass = MutableLiveData(ForgotPass())
    private lateinit var mLoadingDialog: LoadingDialog
    lateinit var resSend: String

    fun initLoadDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context)
    }

    private fun changePass(
        forgotPass: ForgotPass,
        context: Context,
    ) {
        Log.d(TAG, "Params : $forgotPass")
        Log.d(TAG, "token : $mToken")
        mLoadingDialog.showLoading()
        server.forgotPassword(
            forgotPass
        )
            .enqueue(object : retrofit2.Callback<ResponseResult<String>> {
                override fun onResponse(
                    call: retrofit2.Call<ResponseResult<String>>,
                    response: retrofit2.Response<ResponseResult<String>>
                ) {
                    if (response.isSuccessful) {
                        resSend = response.body()?.data.toString()
                    }
                }

                override fun onFailure(call: retrofit2.Call<ResponseResult<String>>, t: Throwable) {

                }

            })
    }

    fun onEvent(event: UserEvent, context: Context) {
        when (event) {
            is UserEvent.OnOldPass -> {
                _forgotPass.value = _forgotPass.value?.copy(
                    mail = event.value
                )
                _validateOldPass.value = event.value.blankException()
            }

            is UserEvent.ValidateForm -> {
                _forgotPass.value?.let {
                    if (it.checkValidate()) {
                        mLoadingDialog.showLoading()
                        changePass(
                            forgotPass = it,
                            context = context,
                        )
                    } else {
                        mLoadingDialog.hideLoading()
                    }
                }
            }
        }
    }

    sealed class UserEvent {
        data class OnOldPass(val value: String) : UserEvent()
        object ValidateForm : UserEvent()
    }
}