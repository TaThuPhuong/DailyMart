package net.fpoly.dailymart.view.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.repository.UserRepository
import net.fpoly.dailymart.utils.SharedPref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val app: Application) :
    ViewModel() {

    private val TAG = "YingMing"
    private val mToken = SharedPref.getAccessToken(app)

    val updateSuccess = MutableLiveData(false)
    val message = MutableLiveData("")

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {

            val api = ServerInstance.apiUser
            try {
                api.updateUser(mToken, user.id, UserRes(user))
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>,
                        ) {
                            response.body()?.string()?.let {
                                Log.e(TAG, "body: $it")
                            }
                            response.errorBody()?.string()?.let {
                                Log.e(TAG, "errorBody: $it")
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.e(TAG, "updateUser onFailure: $t", )
                        }

                    })
            } catch (e: Exception) {
                Log.e(TAG, "updateUser Exception: $e", )
            }
        }
    }
}