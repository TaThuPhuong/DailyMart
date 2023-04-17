package net.fpoly.dailymart.view.staff.details

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.model.param.Datum
import net.fpoly.dailymart.data.model.param.UpdateParam
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.repository.UserRepository
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class DetailStaffViewModel(private val app: Application, private val userRepo: UserRepository) :
    ViewModel() {

    private val TAG = "YingMing"
    private val mToken = SharedPref.getAccessToken(app)
    private val userParam = MutableLiveData(UpdateParam())

    private val _validateName = MutableLiveData("")
    val validateNameUser: LiveData<String> = _validateName
    private val _validatePhone = MutableLiveData("")
    val validatePhone: LiveData<String> = _validatePhone
    private val _validateEmailUser = MutableLiveData("")
    val validateEmailUser: LiveData<String> = _validateEmailUser

    val updateSuccess = MutableLiveData(false)
    val message = MutableLiveData("")

    fun setUser(user: Datum) {
        userParam.value = UpdateParam(user)
        Log.e(TAG, "setUser: ${userParam.value}")
    }

    fun onEvent(event: UpdateEvent) {
        when (event) {
            is UpdateEvent.OnEmailChange -> {
                userParam.value = userParam.value?.copy(
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
            is UpdateEvent.OnNameChange -> {
                userParam.value = userParam.value?.copy(
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
            is UpdateEvent.OnPhoneNumberChange -> {
                userParam.value = userParam.value?.copy(
                    phoneNumber = event.phone
                )
                if (event.phone.trim().isEmpty()) {
                    _validatePhone.value = event.phone.blankException()
                } else if (!isPhoneNumberValid(event.phone)) {
                    _validatePhone.value = "Số điện thoại không hợp lệ!"
                } else {
                    _validatePhone.value = ""
                }

            }
            is UpdateEvent.OnChangeRole -> {
                userParam.value = userParam.value?.copy(
                    role = event.role
                )
            }
            is UpdateEvent.OnChangeStatus -> {
                userParam.value = userParam.value?.copy(
                    status = event.status
                )
            }
            is UpdateEvent.OnUpdate -> {
                userParam.value?.let {
                    if (it.checkValidate()) {
                        if (event.linkAva != null) {
                            userParam.value = userParam.value?.copy(
                                linkAvt = event.linkAva
                            )
                            updateUser(it._id)
                        } else {
                            updateUser(it._id)
                        }
                    } else {
                        updateSuccess.postValue(false)
                        message.postValue("Vui lòng điền đầy đủ thông tin")
                    }
                }
            }
        }
    }

    private fun updateUser(id: String) {
        viewModelScope.launch {
            when (val res = userRepo.updateUser(mToken, id, userParam.value!!)) {
                is Response.Success -> {
                    updateSuccess.postValue(true)
                }
                is Response.Error -> {
                    updateSuccess.postValue(false)
                    message.postValue(res.message)
                }
            }
        }
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val regex =
            Regex("^\\+?(0)([3|5|7|8|9]\\d{8})$")
        return regex.matches(phoneNumber)
    }

    private fun isEmailValid(email: String): Boolean {
        val regex = Regex("^[A-Za-z\\d+_.-]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,}\$")
        return regex.matches(email)
    }
}

sealed class UpdateEvent {
    data class OnNameChange(val name: String) : UpdateEvent()
    data class OnEmailChange(val email: String) : UpdateEvent()
    data class OnPhoneNumberChange(val phone: String) : UpdateEvent()
    data class OnChangeRole(val role: ROLE) : UpdateEvent()
    data class OnChangeStatus(val status: Boolean) : UpdateEvent()
    data class OnUpdate(val linkAva: String?) : UpdateEvent()
}