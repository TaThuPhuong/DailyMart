package net.fpoly.dailymart.view.change_password

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
import net.fpoly.dailymart.data.model.param.ChangePassParam
import net.fpoly.dailymart.data.repository.UserRepositoryImpl
import net.fpoly.dailymart.extension.blankException
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.data.model.Response.Success
import net.fpoly.dailymart.data.model.Response.Error

class ChangePasswordViewModel(app: Application) : ViewModel() {
    private val TAG = "Tuvm"
    private val mToken = SharedPref.getAccessToken(app)
    private val server = ServerInstance.apiUser

    private val changePassRepo = UserRepositoryImpl()
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
        mLoadingDialog.showLoading()
        viewModelScope.launch {
            when (val resPass = changePassRepo.changePass(mToken, changePassParam)) {
                is Success -> {
                    Log.e(TAG, "changePassViewModel: ${resPass.data}")
                    Toast.makeText(context, resPass.message, Toast.LENGTH_SHORT).show()
                    activity?.finish()
                }
                is Error -> {
                    _validateOldPass.value = resPass.message
                    mLoadingDialog.hideLoading()
                }
            }
        }
    }

    fun onEvent(event: UserEvent, context: Context) {
        when (event) {
            is UserEvent.OnOldPass -> {
                _changeParam.value = _changeParam.value?.copy(
                    oldPass = event.value
                )
                if (event.value.isEmpty()) {
                    _validateOldPass.value = event.value.blankException()
                } else {
                    _validateOldPass.value = ""
                }
            }
            is UserEvent.OnNewPass -> {
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
            is UserEvent.OnConfirm -> {
                if (event.value.isEmpty()) {
                    _validateConfirm.value = event.value.blankException()
                } else if (event.value != _changeParam.value?.newPass) {
                    _validateConfirm.value = "Mật khẩu không khớp !"
                } else {
                    _validateConfirm.value = ""
                }
            }

            is UserEvent.ValidateForm -> {
                _changeParam.value?.let {
                    if (it.checkValidate()) {
                        mLoadingDialog.showLoading()
                        changePass(
                            changePassParam = it,
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

    sealed class UserEvent {
        data class OnOldPass(val value: String) : UserEvent()
        data class OnNewPass(val value: String) : UserEvent()
        data class OnConfirm(val value: String) : UserEvent()
        object ValidateForm : UserEvent()
    }

    private fun validatePassword(password: String): Boolean {
        val pattern =
            "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[,.!@#\\/\\$&*~\"|:]).{8,}".toRegex()
        return pattern.matches(password)
    }
}