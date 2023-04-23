package net.fpoly.dailymart.view.change_password

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.param.ChangePassParam
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.data.model.Response.Success
import net.fpoly.dailymart.data.model.Response.Error
import net.fpoly.dailymart.repository.UserRepository

class ChangePasswordViewModel(app: Application, private val userRepo: UserRepository) :
    ViewModel() {
    private val TAG = "Tuvm"
    private val mToken = SharedPref.getAccessToken(app)
    private val _validateOldPass = MutableLiveData("")
    val validateOldPass: LiveData<String> = _validateOldPass
    private val _validateNewPass = MutableLiveData("")
    val validateNewPass: LiveData<String> = _validateNewPass
    private val _validateConfirm = MutableLiveData("")
    val validateConfirm: LiveData<String> = _validateConfirm
    private val _changeParam = MutableLiveData(ChangePassParam())
    private lateinit var mLoadingDialog: LoadingDialog
    private val _passwordOld = MutableLiveData(false)
    val passwordOld: LiveData<Boolean> = _passwordOld
    private val _passwordNew = MutableLiveData(false)
    val passwordNew: LiveData<Boolean> = _passwordNew
    private val _passwordConfirm = MutableLiveData(false)
    val passwordConfirm: LiveData<Boolean> = _passwordConfirm

    val updateSuccess = MutableLiveData(false)
    val message = MutableLiveData("")

    fun initLoadDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context)
    }

    private fun changePass(
        changePassParam: ChangePassParam,
    ) {
        mLoadingDialog.showLoading()
        viewModelScope.launch {
            when (val resPass = userRepo.changePass(mToken, changePassParam)) {
                is Success -> {
                    updateSuccess.postValue(true)
                    Log.e(TAG, "changePassViewModel: ${resPass.data}")
                }

                is Error -> {
                    updateSuccess.postValue(false)
                    message.postValue(resPass.message)
                }
            }
        }
    }

    fun onEvent(event: ChangeEvent, context: Context) {
        when (event) {
            is ChangeEvent.OnOldPass -> {
                _changeParam.value = _changeParam.value?.copy(
                    oldPass = event.value
                )
                if (event.value.isEmpty()) {
                    _validateOldPass.value = event.value.blankException()
                } else {
                    _validateOldPass.value = ""
                }
            }

            is ChangeEvent.OnNewPass -> {
                _changeParam.value = _changeParam.value?.copy(
                    newPass = event.value
                )
                if (event.value.isEmpty()) {
                    _validateNewPass.value = event.value.blankException()
                } else if (event.value == _changeParam.value?.oldPass) {
                    _validateNewPass.value = "Mật khẩu mới không được trùng với mật khẩu cũ"
                } else if (!validatePassword(event.value)) {
                    _validateNewPass.value =
                        "Mật khẩu tối thiểu 8 kí tự có chữ hoa chữ thường, số và kí tự đặc biệt"
                } else {
                    _validateNewPass.value = ""
                }
            }

            is ChangeEvent.OnConfirm -> {
                if (event.value.isEmpty()) {
                    _validateConfirm.value = event.value.blankException()
                } else if (event.value != _changeParam.value?.newPass) {
                    _validateConfirm.value = "Mật khẩu không khớp !"
                } else {
                    _validateConfirm.value = ""
                }
            }

            is ChangeEvent.OnPassOld -> {
                _passwordOld.value = !_passwordOld.value!!
            }

            is ChangeEvent.OnPassNew -> {
                _passwordNew.value = !_passwordNew.value!!
            }

            is ChangeEvent.OnPassConfirm -> {
                _passwordConfirm.value = !_passwordConfirm.value!!
            }

            is ChangeEvent.ValidateForm -> {
                _changeParam.value?.let {
                    if (it.checkValidate()) {
                        changePass(
                            changePassParam = it,
                        )
                    } else {
                        updateSuccess.postValue(false)
                        message.postValue("Vui lòng điền đầy đủ và đúng thông tin")
                    }
                }
            }
        }
    }

    sealed class ChangeEvent {
        data class OnOldPass(val value: String) : ChangeEvent()
        data class OnNewPass(val value: String) : ChangeEvent()
        data class OnConfirm(val value: String) : ChangeEvent()
        object OnPassOld : ChangeEvent()
        object OnPassNew : ChangeEvent()
        object OnPassConfirm : ChangeEvent()
        object ValidateForm : ChangeEvent()
    }

    private fun validatePassword(password: String): Boolean {
        val pattern =
            "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[,.!@#\\/\\$&*~\"|:]).{8,}".toRegex()
        return pattern.matches(password)
    }
}