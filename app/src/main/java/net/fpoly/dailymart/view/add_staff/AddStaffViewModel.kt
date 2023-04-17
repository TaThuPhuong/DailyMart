package net.fpoly.dailymart.view.add_staff

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.data.repository.UserRepositoryImpl
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.data.model.Response.Error
import net.fpoly.dailymart.data.model.Response.Success
import net.fpoly.dailymart.repository.UserRepository
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.staff.details.UpdateEvent
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStaffViewModel(
    private val app: Application,
    private val userRepo: UserRepository,
) : ViewModel() {

    val TAG = "YingMing"

    private val _validateName = MutableLiveData("")
    val validateNameUser: LiveData<String> = _validateName
    private val _validatePhone = MutableLiveData("")
    val validatePhone: LiveData<String> = _validatePhone
    private val _validateEmailUser = MutableLiveData("")
    val validateEmailUser: LiveData<String> = _validateEmailUser
    private val _userParam = MutableLiveData(RegisterParam())
    private val mToken = SharedPref.getAccessToken(app)
    val addStaffSuccess = MutableLiveData(false)
    val message = MutableLiveData("")

    init {
        _validateName.value = ""
        _validatePhone.value = ""
        _validateEmailUser.value = ""
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.OnNameChange -> {
                _userParam.value = _userParam.value?.copy(
                    name = event.name
                )
                if (event.name.trim().isEmpty()) {
                    _validateName.value = event.name.blankException()
                } else if (event.name.length < 3) {
                    _validateName.value = "Tên từ 4 kí tự"
                } else {
                    _validateName.value = ""
                }
            }
            is UserEvent.OnPhoneNumberChange -> {
                _userParam.value = _userParam.value?.copy(
                    phoneNumber = event.phone,
                    password = event.phone
                )
                if (event.phone.trim().isEmpty()) {
                    _validatePhone.value = event.phone.blankException()
                } else if (!isPhoneNumberValid(event.phone)) {
                    _validatePhone.value = "Số điện thoại không hợp lệ!"
                } else {
                    _validatePhone.value = ""
                }
            }
            is UserEvent.OnEmailChange -> {
                _userParam.value = _userParam.value?.copy(
                    email = event.email
                )
                if (event.email.trim().isEmpty()) {
                    _validateEmailUser.value = event.email.blankException()
                } else if (!isEmailValid(event.email)) {
                    _validateEmailUser.value = "Email không hợp lệ!"
                } else {
                    _validateEmailUser.value = ""
                }
            }
            is UserEvent.OnChangeRole -> {
                _userParam.value = _userParam.value?.copy(
                    role = event.role
                )
            }
            is UserEvent.CreateUser -> {
                _userParam.value?.let {
                    if (it.checkValidate()) {
                        postUser(it)
                    } else {
                        addStaffSuccess.value = false
                    }
                }
            }
        }
    }

    private fun postUser(userParam: RegisterParam) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val res = userRepo.createUser(mToken, userParam)) {
                is Error -> {
                    message.postValue(res.message)
                }
                is Success -> {
                    addStaffSuccess.postValue(true)
                }
            }
        }
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

sealed class UserEvent {
    data class OnNameChange(val name: String) : UserEvent()
    data class OnEmailChange(val email: String) : UserEvent()
    data class OnPhoneNumberChange(val phone: String) : UserEvent()
    data class OnChangeRole(val role: ROLE) : UserEvent()
    object CreateUser : UserEvent()
}