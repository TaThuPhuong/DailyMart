package net.fpoly.dailymart.view.profile

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.repository.UserRepository
import net.fpoly.dailymart.utils.SharedPref

class ProfileViewModel(private val app: Application, private val repo: UserRepository) :
    ViewModel() {

    private val mToken = SharedPref.getAccessToken(app)

    val updateSuccess = MutableLiveData(false)
    val message = MutableLiveData("")

     fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.updateUser(mToken, user.id, UserRes(user))
            if (res.isSuccess()) {
                updateSuccess.postValue(true)
                message.postValue("Cập nhập thành công")
            } else {
                updateSuccess.postValue(false)
                message.postValue("Cập nhập thất bại")
            }
        }
    }
}