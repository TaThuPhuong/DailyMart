package net.fpoly.dailymart.view.change_password

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.param.ChangePassParam
import net.fpoly.dailymart.utils.SharedPref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class ChangePasswordViewModel(app: Application) : ViewModel() {
    private val TAG = "Tuvm"
    private val mToken = SharedPref.getAccessToken(app)
    private val server = ServerInstance.apiUser

    fun changePass(changePassParam: ChangePassParam) {
        Log.d(TAG, "Params : $changePassParam")
        Log.d(TAG, "token : $mToken")
        server.changePassword(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY0MmVkNTkyM2Q2NTQ3M2IxZTQ3MmEyOSIsInJvbGUiOiJzdGFmZiIsImlhdCI6MTY4MDc5MTUxOCwiZXhwIjoxNzY3MTA1MTE4fQ.YtEF4YwRwsX9owAVJcOwahgZ9TTjeEoqyBQyuteNy_8",
            changePassParam
        )
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "Response : " + response.body().toString())
                        Log.d(TAG, "code: " + response.code())
                        Log.d(TAG, "message: " + response.message())
                        Log.d(TAG, "errorBody: " + response.errorBody()?.string())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e(TAG, "Response : " + t.message)
                }

            })
    }
}