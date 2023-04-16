package net.fpoly.dailymart.view.add_staff

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
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.data.repository.UserRepositoryImpl
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.data.model.Response.Error
import net.fpoly.dailymart.data.model.Response.Success
import net.fpoly.dailymart.utils.SharedPref

class AddStaffViewModel(
    private val app: Application,
) : ViewModel() {
    val TAG = "tuvm";
    private val userRepo = UserRepositoryImpl()
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private val _validateName = MutableLiveData("")
    val validateNameUser: LiveData<String> = _validateName
    private val _validatePhone = MutableLiveData("")
    val validatePhone: LiveData<String> = _validatePhone
    private val _validateEmailUser = MutableLiveData("")
    val validateEmailUser: LiveData<String> = _validateEmailUser
    private val _userParam = MutableLiveData(RegisterParam())
    private val mToken = SharedPref.getAccessToken(app)

    init {
        _validateName.value = ""
        _validatePhone.value = ""
        _validateEmailUser.value = ""
    }

    val addStaffSuccess = MutableLiveData(false)
    private lateinit var mLoadingDialog: LoadingDialog

    fun initLoadDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context)
    }

    fun postUser(userParam: RegisterParam, context: Context, activity: AddStaffActivity?) {
        viewModelScope.launch {
            mLoadingDialog.showLoading()
            val res = userRepo.createUser(mToken, userParam)
            when (res) {
                is Success -> {
                    Log.e(TAG, "postUser: ${res.data}")
                    activity?.finish()
                }
                is Error -> {
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
                if (event.value.trim().isEmpty()) {
                    _validateName.value = event.value.blankException()
                } else if (event.value.length < 3) {
                    _validateName.value = "Tên không được bé hơn 3 kí tự"
                } else {
                    _validateName.value = ""
                }
            }
            is UserEvent.OnPhoneNumberChange -> {
                _userParam.value = _userParam.value?.copy(
                    phoneNumber = event.value
                )
                if (event.value.trim().isEmpty()) {
                    _validatePhone.value = event.value.blankException()
                } else if (!isPhoneNumberValid(event.value)) {
                    _validatePhone.value = "Số điện thoại không hợp lệ!"
                } else {
                    _validatePhone.value = ""
                }
            }
            is UserEvent.OnEmail -> {
                _userParam.value = _userParam.value?.copy(
                    email = event.value
                )
                if (event.value.trim().isEmpty()) {
                    _validateEmailUser.value = event.value.blankException()
                } else if (!isEmailValid(event.value)) {
                    _validateEmailUser.value = "Email không hợp lệ!"
                } else {
                    _validateEmailUser.value = ""
                }
            }

            is UserEvent.ValidateForm -> {
                _userParam.value?.let {
                    if (it.checkValidate()) {

                    } else {
                        addStaffSuccess.value = false
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

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val regex =
            Regex("^\\+?(0)([3|5|7|8|9]\\d{8})$")  // Biểu thức chính quy kiểm tra số điện thoại
        return regex.matches(phoneNumber)
    }

    private fun isEmailValid(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return regex.matches(email)
    }
}