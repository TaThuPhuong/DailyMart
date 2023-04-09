package net.fpoly.dailymart.view.staff

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.data.model.param.Datum
import net.fpoly.dailymart.data.model.param.UserModel
import okhttp3.ResponseBody
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
        server.getAllUser("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY0MmVkNTkyM2Q2NTQ3M2IxZTQ3MmEyOSIsInJvbGUiOiJzdGFmZiIsImlhdCI6MTY4MDc5MTUxOCwiZXhwIjoxNzY3MTA1MTE4fQ.YtEF4YwRwsX9owAVJcOwahgZ9TTjeEoqyBQyuteNy_8")
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

    fun updateUser(id: String, userParams: Datum, context: Context) {
        val serverInstance = ServerInstance.apiUser
        serverInstance.updateUser2(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY0MmVkNTkyM2Q2NTQ3M2IxZTQ3MmEyOSIsInJvbGUiOiJzdGFmZiIsImlhdCI6MTY4MDc5MTUxOCwiZXhwIjoxNzY3MTA1MTE4fQ.YtEF4YwRwsX9owAVJcOwahgZ9TTjeEoqyBQyuteNy_8",
            id,
            userParams
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Thanh Cong", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Update user res : " + response.message())
                    Log.d(TAG, "Update user res : " + response.body().toString())
                    Log.d(TAG, "Update user res : " + response.errorBody())
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "Update user error : " + t.message)
            }

        })
    }

    init {
        getUser()
    }
}