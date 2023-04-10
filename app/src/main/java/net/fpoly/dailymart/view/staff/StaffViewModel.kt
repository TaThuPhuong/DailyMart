package net.fpoly.dailymart.view.staff

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.data.model.param.Datum
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.data.model.param.UserModel
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.staff.details.DetailsStaffActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StaffViewModel(
    private val app: Application,
) : ViewModel() {
    val TAG = "tuvm";
    private val _user = MutableLiveData<List<Datum>>()
    val user: LiveData<List<Datum>> = _user
    private val mToken = SharedPref.getAccessToken(app)
    private val _validateName = MutableLiveData("")
    val validateNameUser: LiveData<String> = _validateName
    private val _validatePhone = MutableLiveData("")
    val validatePhone: LiveData<String> = _validatePhone
    private val _validateEmailUser = MutableLiveData("")
    val validateEmailUser: LiveData<String> = _validateEmailUser

    private val _userParam = MutableLiveData(RegisterParam())

    val loginSuccess = MutableLiveData(false)
    private lateinit var mLoadingDialog: LoadingDialog

    fun initLoadDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context)
    }

    init {
        _validateName.value = ""
        _validatePhone.value = ""
        _validateEmailUser.value = ""
    }

    fun getUser() {
        val server = ServerInstance.apiUser
        Log.d(TAG, "start ")
        mLoadingDialog.showLoading()
        server.getAllUser(mToken)
            .enqueue(object : Callback<UserModel> {
                override fun onResponse(
                    call: Call<UserModel>,
                    response: Response<UserModel>
                ) {
                    if (response.isSuccessful) {
                        loginSuccess.postValue(true)
                        mLoadingDialog.hideLoading()
                        _user.value = response.body()?.data;
                        Log.d(TAG, "onResponse: " + response.body()?.data)
                    } else {
                        Log.d(TAG, "code: " + response.code())
                        Log.d(TAG, "message: " + response.message())
                        Log.d(TAG, "errorBody: " + response.errorBody()?.string())
                    }
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Log.d(TAG, "message: " + t.message)
                    mLoadingDialog.hideLoading()
                    loginSuccess.postValue(false)
                }

            })
    }

    fun updateUser(id: String?, userParams: RegisterParam, context: Context, activity: DetailsStaffActivity?) {
        val serverInstance = ServerInstance.apiUser
        mLoadingDialog.showLoading()
        serverInstance.updateUser2(
            mToken,
            id!!,
            userParams
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    loginSuccess.postValue(true)
                    Toast.makeText(context, "Thanh Cong", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Update user res : " + response.message())
                    Log.d(TAG, "Update user res : " + response.body().toString())
                    Log.d(TAG, "Update user res : " + response.errorBody())
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                    mLoadingDialog.hideLoading()
                    activity?.finish()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "Update user error : " + t.message)
                loginSuccess.postValue(false)
                mLoadingDialog.hideLoading()
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
                        updateUser(userParams = it, context = context, id = it._id, activity = null);
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