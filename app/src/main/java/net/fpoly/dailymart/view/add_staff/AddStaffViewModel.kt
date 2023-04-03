package net.fpoly.dailymart.view.add_staff

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.repository.UserRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStaffViewModel(
) : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun postUser(userParam: RegisterParam) {
        val server = ServerInstance.apiUser
        server.register(userParam).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                response.isSuccessful
                Log.d("tuvm", "onResponse: " + response.body()?.string())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                print(t.message)
            }

        })
    }
}