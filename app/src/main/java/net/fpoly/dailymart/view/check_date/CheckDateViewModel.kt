package net.fpoly.dailymart.view.check_date

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.ExpiryCheck
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.param.DeleteExpiryParam
import net.fpoly.dailymart.repository.ProductRepository
import net.fpoly.dailymart.utils.SharedPref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckDateViewModel(private val app: Application, private val productRepo: ProductRepository) :
    ViewModel() {

    private val TAG = "YingMing"
    private val mToken = SharedPref.getAccessToken(app)
    private val _listProduct = MutableLiveData<List<Product>>(ArrayList())
    val listProduct: LiveData<List<Product>> = _listProduct

    val message = MutableLiveData("")
    fun getListProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = productRepo.getAllProduct(mToken)
            if (res.isSuccess()) {
                _listProduct.postValue(res.data!!)
            } else {
                message.postValue("Đã xảy ra lỗi")
            }
        }
    }

    fun onDestroyProduct(expiry: ExpiryCheck) {
        val api = ServerInstance.apiProduct
        viewModelScope.launch(Dispatchers.IO) {
            try {
                api.deleteExpiry(mToken, DeleteExpiryParam(expiry.id))
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>,
                        ) {
                            if (response.isSuccessful) {
                                Log.e(TAG, "body: ${response.body()?.string()}")
                                Log.e(TAG, "errorBody: ${response.errorBody()?.string()}")
                                getListProduct()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                        }
                    })
            } catch (e: Exception) {
                Log.e(TAG, "errorBody: $e")
            }
        }
    }
}