package net.fpoly.dailymart.view.forget_password

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
import net.fpoly.dailymart.data.model.param.ForgotPass
import net.fpoly.dailymart.data.model.response.ResponseResult
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.login.LoginActivity
import net.fpoly.dailymart.view.reset_password.ResetPasswordActivity
import net.fpoly.dailymart.view.task.detail_product.ProductDetailActivity

class ForgetPassViewModel(app: Application) : ViewModel() {
    private val TAG = "Tuvm"
    private val mToken = SharedPref.getAccessToken(app)
    private val server = ServerInstance.apiUser

    private val _validateOldPass = MutableLiveData("")
    val validateOldPass: LiveData<String> = _validateOldPass
    private val _forgotPass = MutableLiveData(ForgotPass())
    private lateinit var mLoadingDialog: LoadingDialog
    var resSend = String()

    fun initLoadDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context)
    }

    fun sendOTP(
        forgotPass: ForgotPass,
        context: Context,
        activity: ForgetPasswordActivity?
    ) {
        Log.d(TAG, "Params : $forgotPass")
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
                        Log.e(TAG, "id = $resSend")
                        Log.d(TAG, "Response send message : " + response.message())
                        Log.d(TAG, "Response send error body : " + response.errorBody())
                        Log.d(TAG, "Response send body : " + response.body())
                        Toast.makeText(context, response.message().toString(), Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(context, ResetPasswordActivity::class.java)
                        intent.putExtra("id", resSend)
                        activity?.startActivity(intent)
                        mLoadingDialog.hideLoading()
                    } else {
                        Log.d(TAG, "Response send error : " + response.errorBody().toString())
                        mLoadingDialog.hideLoading()
                    }
                }

                override fun onFailure(call: retrofit2.Call<ResponseResult<String>>, t: Throwable) {
                    Log.d(TAG, "Response send error fail : " + t.message.toString())
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    mLoadingDialog.hideLoading()
                }

            })
    }

    fun onEvent(event: ForgotEvent, context: Context) {
        when (event) {
            is ForgotEvent.OnEmail -> {
                _forgotPass.value = _forgotPass.value?.copy(
                    mail = event.value
                )
                _validateOldPass.value = event.value.blankException()
            }

            is ForgotEvent.ValidateForm -> {
                _forgotPass.value?.let {
                    if (it.checkValidate()) {
                        mLoadingDialog.showLoading()
                        sendOTP(
                            forgotPass = it,
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

    sealed class ForgotEvent {
        data class OnEmail(val value: String) : ForgotEvent()
        object ValidateForm : ForgotEvent()
    }
}