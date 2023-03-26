package net.fpoly.dailymart.view.add_staff

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.repository.UserRepository
import net.fpoly.dailymart.utils.ROLE

class AddStaffViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val newUser = MutableLiveData(User())

    fun saveUser(user: User, context: Context) {
        viewModelScope.launch {
            userRepository.insertUser(user)
            Toast.makeText(context, "Them thanh cong", Toast.LENGTH_LONG).show()
        }
    }
}