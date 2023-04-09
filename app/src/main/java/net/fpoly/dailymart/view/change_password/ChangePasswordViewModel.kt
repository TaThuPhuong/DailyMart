package net.fpoly.dailymart.view.change_password

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.param.ChangePassParam
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.staff.details.DetailsStaffActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class ChangePasswordViewModel(app: Application) : ViewModel() {
    private val TAG = "Tuvm"
    private val mToken = SharedPref.getAccessToken(app)
    private val server = ServerInstance.apiUser

    fun changePass(
        changePassParam: ChangePassParam,
        context: Context,
        activity: ChangePasswordActivity?
    ) {
        Log.d(TAG, "Params : $changePassParam")
        Log.d(TAG, "token : $mToken")
        server.changePassword(
            mToken,
            changePassParam
        )
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Đổi mật khẩu thành công !", Toast.LENGTH_LONG)
                            .show()
                        Log.d(TAG, "Response : " + response.body().toString())
                        Log.d(TAG, "code: " + response.code())
                        Log.d(TAG, "message: " + response.message())
                        Log.d(TAG, "errorBody: " + response.errorBody()?.string())
                        activity?.finish()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e(TAG, "Response : " + t.message)
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    activity?.finish()
                }

            })
    }
}