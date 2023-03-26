package net.fpoly.dailymart.view.staff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.repository.UserRepository
import net.fpoly.dailymart.utils.ROLE

class StaffViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _listUser = MutableLiveData<List<User>>(ArrayList())
    val listUser: LiveData<List<User>> = _listUser

    init {
        getListUser()
    }

    private fun getListUser() {
        viewModelScope.launch {
            userRepository.getUserByRole(ROLE.STAFF)?.collect { users ->
                _listUser.value = users
            }
        }
    }

}