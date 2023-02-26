package net.fpoly.dailymart.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    private val _passwordStatus = MutableLiveData(PasswordStatus())
    val passwordStatus: LiveData<PasswordStatus> = _passwordStatus

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.ShowPassword -> {
                _passwordStatus.value = _passwordStatus.value?.copy(
                    isShowPassword = !_passwordStatus.value!!.isShowPassword
                )
            }
            is RegisterEvent.ShowConfirmPassword -> {
                _passwordStatus.value = _passwordStatus.value?.copy(
                    isShowConfirmPassword = !_passwordStatus.value!!.isShowConfirmPassword
                )
            }
        }
    }

}

sealed class RegisterEvent {
    object ShowPassword : RegisterEvent()
    object ShowConfirmPassword : RegisterEvent()
}

data class PasswordStatus(
    val isShowPassword: Boolean = false,
    val isShowConfirmPassword: Boolean = false,
)