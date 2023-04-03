package net.fpoly.dailymart.view.staff

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.utils.ROLE

class StaffViewModel() : ViewModel() {
    private val _listUser = MutableLiveData<List<User>>(ArrayList())
    val listUser: LiveData<List<User>> = _listUser
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    val updateUser = MutableLiveData(User())

    init {
        getListUser()
    }

    private fun getListUser() {
        viewModelScope.launch {

        }
    }

    fun saveUser(user: User, context: Context) {

    }
}