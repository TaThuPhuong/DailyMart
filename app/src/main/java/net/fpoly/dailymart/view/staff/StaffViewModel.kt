package net.fpoly.dailymart.view.staff

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.data.model.param.Datum
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.data.model.param.UpdateParam
import net.fpoly.dailymart.data.model.param.UserModel
import net.fpoly.dailymart.data.model.succeeded
import net.fpoly.dailymart.data.repository.UserRepositoryImpl
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
    private val userRepo = UserRepositoryImpl()
    private val _user = MutableLiveData<List<Datum>>()
    val user: LiveData<List<Datum>> = _user
    private val mToken = SharedPref.getAccessToken(app)
    private val _validateName = MutableLiveData("")
    val validateNameUser: LiveData<String> = _validateName
    private val _validatePhone = MutableLiveData("")
    val validatePhone: LiveData<String> = _validatePhone
    private val _validateEmailUser = MutableLiveData("")
    val validateEmailUser: LiveData<String> = _validateEmailUser

    private val _userParam = MutableLiveData(UpdateParam())

    val updateSuccess = MutableLiveData(false)
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
                        updateSuccess.postValue(true)
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
                    updateSuccess.postValue(false)
                }

            })
    }

    fun updateUser(
        id: String,
        userParams: UpdateParam,
        context: Context,
        activity: DetailsStaffActivity?
    ) {
        viewModelScope.launch {
            mLoadingDialog.showLoading()
            val res = userRepo.updateUser(mToken, id, updateParam = userParams)
            when (res) {
                is net.fpoly.dailymart.data.model.Response.Success -> {
                    Log.e(TAG, "updateUser: ${res.data}")
                    activity?.finish()
                }
                is net.fpoly.dailymart.data.model.Response.Error -> {
                    Toast.makeText(context, res.message, Toast.LENGTH_SHORT).show()
                    mLoadingDialog.hideLoading()
                }
            }
        }
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
                    } else {
                        updateSuccess.value = false
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