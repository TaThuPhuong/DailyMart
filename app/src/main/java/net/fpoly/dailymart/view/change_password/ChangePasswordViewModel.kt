package net.fpoly.dailymart.view.change_password

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.param.ChangePassParam
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.staff.details.DetailsStaffActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class ChangePasswordViewModel(app: Application) : ViewModel() {
    private val TAG = "Tuvm"
    private val mToken = SharedPref.getAccessToken(app)
    private val server = ServerInstance.apiUser

    private val _validateOldPass = MutableLiveData("")
    val validateOldPass: LiveData<String> = _validateOldPass
    private val _validateNewPass = MutableLiveData("")
    val validateNewPass: LiveData<String> = _validateNewPass
    private val _validateConfirm = MutableLiveData("")
    val validateConfirm: LiveData<String> = _validateConfirm
    private val _changeParam = MutableLiveData(ChangePassParam())
    private lateinit var mLoadingDialog: LoadingDialog

    fun initLoadDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context)
    }

    fun changePass(
        changePassParam: ChangePassParam,
        context: Context,
        activity: ChangePasswordActivity?
    ) {
        Log.d(TAG, "Params : $changePassParam")
        Log.d(TAG, "token : $mToken")
        mLoadingDialog.showLoading()
        server.changePassword(
            mToken,
            changePassParam
        )
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Đổi mật khẩu thành công !", Toast.LENGTH_LONG)
                            .show()
                        Log.d(TAG, "Response : " + response.body().toString())
                        Log.d(TAG, "code: " + response.code())
                        Log.d(TAG, "message: " + response.message())
                        Log.d(TAG, "errorBody: " + response.errorBody()?.string())
                        activity?.finish()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e(TAG, "Response : " + t.message)
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    activity?.finish()
                }
            })
    }

    fun onEvent(event: UserEvent, context: Context) {
        when (event) {
            is UserEvent.OnNewPass -> {
                _changeParam.value = _changeParam.value?.copy(
                    oldPass = event.value
                )
                _validateOldPass.value = event.value.blankException()
            }
            is UserEvent.OnOldPass -> {
                _changeParam.value = _changeParam.value?.copy(
                    newPass = event.value
                )
                _validateNewPass.value = event.value.blankException()
            }
            is UserEvent.OnConfirm -> {
                _changeParam.value = _changeParam.value?.copy(
                    newPass = event.value
                )
                _validateNewPass.value = event.value.blankException()
            }

            is UserEvent.ValidateForm -> {
                _changeParam.value?.let {
                    if (it.checkValidate()) {
                        mLoadingDialog.showLoading()
                        changePass(
                            changePassParam = it,
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

    sealed class UserEvent {
        data class OnOldPass(val value: String) : UserEvent()
        data class OnNewPass(val value: String) : UserEvent()
        data class OnConfirm(val value: String) : UserEvent()
        object ValidateForm : UserEvent()
    }
}