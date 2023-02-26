package net.fpoly.dailymart.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val TAG = "YingMing"

    private val _passwordStatus = MutableLiveData(false)
    val passwordStatus: LiveData<Boolean> = _passwordStatus

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.ShowPassword -> {
                _passwordStatus.value = !_passwordStatus.value!!
                Log.d(TAG, "onEvent: ")
            }
            is LoginEvent.Login -> {

            }
        }
    }

}

sealed class LoginEvent {
    object ShowPassword : LoginEvent()
    object Login : LoginEvent()
}