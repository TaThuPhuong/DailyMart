package net.fpoly.dailymart.view.add_staff

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.view.login.LoginEvent
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStaffViewModel(
) : ViewModel() {
    val TAG = "tuvm";
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    val _validateName = MutableLiveData("")
    val validateNameUser: LiveData<String> = _validateName
    private val _validatePhone = MutableLiveData("")
    val validatePhone: LiveData<String> = _validatePhone
    private val _validateEmailUser = MutableLiveData("")
    val validateEmailUser: LiveData<String> = _validateEmailUser
    private val _userParam = MutableLiveData(RegisterParam())

    init {
        _validateName.value = ""
        _validatePhone.value = ""
        _validateEmailUser.value = ""
    }

    val loginSuccess = MutableLiveData(false)
    private lateinit var mLoadingDialog: LoadingDialog

    fun initLoadDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context)
    }

    fun postUser(userParam: RegisterParam, context: Context) {
        val server = ServerInstance.apiUser
        Log.d(TAG, "userParam: $userParam")
        server.register(
            userParam,
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY0MmVkNTkyM2Q2NTQ3M2IxZTQ3MmEyOSIsInJvbGUiOiJzdGFmZiIsImlhdCI6MTY4MDc5MTUxOCwiZXhwIjoxNzY3MTA1MTE4fQ.YtEF4YwRwsX9owAVJcOwahgZ9TTjeEoqyBQyuteNy_8"
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    loginSuccess.value = true
                    Log.d(TAG, "onResponse: " + response.body()?.string())
                    Log.d(TAG, "onResponse: " + response.body())
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                } else {
                    Log.d(TAG, "code: " + response.code())
                    Log.d(TAG, "message: " + response.message())
                    Log.d(TAG, "errorBody: " + response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                print(t.message)
            }

        })
    }

    fun onEvent(event: UserEvent, context: Context) {
        when (event) {
            is UserEvent.OnNameUser -> {
                _userParam.value = _userParam.value?.copy(
                    name = event.value
                )
                _validateName.value = event.value.blankException()
            }
            is UserEvent.OnPhoneNumberChange -> {
                _userParam.value = _userParam.value?.copy(
                    phoneNumber = event.value
                )
                _validatePhone.value = event.value.blankException()
            }
            is UserEvent.OnEmail -> {
                _userParam.value = _userParam.value?.copy(
                    email = event.value
                )
                _validateEmailUser.value = event.value.blankException()
            }

            is UserEvent.ValidateForm -> {
                _userParam.value?.let {
                    if (it.checkValidate()) {
                        mLoadingDialog.showLoading()
                        postUser(it, context = context)
                    } else {
                        loginSuccess.value = false
                    }
                }
            }
        }
    }

    sealed class UserEvent {
        data class OnNameUser(val value: String) : UserEvent()
        data class OnEmail(val value: String) : UserEvent()
        data class OnPhoneNumberChange(val value: String) : UserEvent()
        object ValidateForm : UserEvent()
    }
}