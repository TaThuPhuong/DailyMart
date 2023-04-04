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
    val TAG = "tuvm";
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun postUser(userParam: RegisterParam) {
        val server = ServerInstance.apiUser
        Log.d(TAG, "userParam: $userParam")
        server.register(userParam).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: " + response.body()?.string())
                    Log.d(TAG, "onResponse: " + response.body())
                } else {
                    Log.d(TAG, "code: " + response.code())
                    Log.d(TAG, "message: " + response.message())
                    Log.d(TAG, "errorBody: " + response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                print(t.message)
            }

        })
    }
}