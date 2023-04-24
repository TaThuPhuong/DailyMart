package net.fpoly.dailymart.view.staff

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.param.Datum
import net.fpoly.dailymart.data.model.param.UserModel
import net.fpoly.dailymart.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StaffViewModel(
    private val app: Application,
) : ViewModel() {
    val TAG = "tuvm";
    private val _listUser = MutableLiveData<List<Datum>>()
    val mListUser: LiveData<List<Datum>> = _listUser
    private val mToken = SharedPref.getAccessToken(app)
    private val listUser: ArrayList<Datum> = ArrayList()
    val getUserSuccess = MutableLiveData(false)

    fun getUser() {
        val server = ServerInstance.apiUser
        Log.d(TAG, "start ")
        try {
            server.getAllUser(mToken)
                .enqueue(object : Callback<UserModel> {
                    override fun onResponse(
                        call: Call<UserModel>,
                        response: Response<UserModel>,
                    ) {
                        if (response.isSuccessful) {
                            getUserSuccess.postValue(true)
                            _listUser.value = response.body()?.data?.sortedByDescending { it.status }
                        } else {
                            Log.d(TAG, "code: " + response.code())
                            Log.d(TAG, "message: " + response.message())
                            Log.d(TAG, "errorBody: " + response.errorBody()?.string())
                        }
                    }

                    override fun onFailure(call: Call<UserModel>, t: Throwable) {
                        Log.d(TAG, "message: " + t.message)
                        getUserSuccess.postValue(false)
                    }

                })
        } catch (e: Exception) {
            getUserSuccess.postValue(false)
        }

    }
}