package net.fpoly.dailymart.view.staff

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.param.Datum
import net.fpoly.dailymart.data.model.param.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StaffViewModel(

) : ViewModel() {
    val TAG = "tuvm";
    private val _user = MutableLiveData<List<Datum>>()
    val user: LiveData<List<Datum>> = _user

    private fun getUser() {
        val server = ServerInstance.apiUser
        Log.d(TAG, "start ")
        server.getAllUser("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoibWFuYWdlciIsImlhdCI6MTY4MDUzNjkxNCwiZXhwIjoxNjgwNjIzMzE0fQ.XcrkWCEaXgR95m9BK-MdrCld8JDjwJqgSQ4XrNQ_T1g")
            .enqueue(object : Callback<UserModel> {
                override fun onResponse(
                    call: Call<UserModel>,
                    response: Response<UserModel>
                ) {
                    if (response.isSuccessful) {
                        _user.value = response.body()?.data;
                        Log.d(TAG, "onResponse: " + response.body()?.data)
//                        Log.d(TAG, "onResponse: " + response.body())
                    } else {
                        Log.d(TAG, "code: " + response.code())
                        Log.d(TAG, "message: " + response.message())
                        Log.d(TAG, "errorBody: " + response.errorBody()?.string())
                    }
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Log.d(TAG, "message: " + t.message)
                }

            })
    }

    init {
        getUser()
    }
}