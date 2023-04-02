package net.fpoly.dailymart.view.add_staff

import UserRepositor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.repository.UserRepository

class AddStaffViewModel(
    private val userRepository: UserRepositor
) : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun postUser(userId: RegisterParam) {
        viewModelScope.launch {
            val result = userRepository.postUser(userId)
            _user.value = result
        }
    }
}