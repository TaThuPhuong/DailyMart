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
import net.fpoly.dailymart.data.model.Losses
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.param.DeleteExpiryParam
import net.fpoly.dailymart.firbase.database.LossesDao
import net.fpoly.dailymart.repository.ProductRepository
import net.fpoly.dailymart.utils.SharedPref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class CheckDateViewModel(private val app: Application, private val productRepo: ProductRepository) :
    ViewModel() {

    private val TAG = "YingMing"
    private val mToken = SharedPref.getAccessToken(app)
    private val _listProduct = MutableLiveData<List<Product>>(ArrayList())
    val listProductCheckDate: LiveData<List<Product>> = _listProduct
    val getListSuccess = MutableLiveData(false)

    val message = MutableLiveData("")
    fun getListProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = productRepo.getAllProduct(mToken)
            if (res.isSuccess()) {
                res.data?.let { listProduct ->
                    Log.e(TAG, "listProduct: $listProduct")
                    _listProduct.postValue(listProduct.filter { it.totalQuantity > 0 && it.status })
                }
                getListSuccess.postValue(true)
            } else {
                message.postValue("Đã xảy ra lỗi")
                getListSuccess.postValue(true)
            }
        }
    }

    fun onDestroyProduct(expiry: ExpiryCheck) {
        val api = ServerInstance.apiProduct
        val cal = Calendar.getInstance()
        val month = cal[Calendar.MONTH] + 1
        val year = cal[Calendar.YEAR]
        viewModelScope.launch(Dispatchers.IO) {
            try {
                api.deleteExpiry(mToken, DeleteExpiryParam(expiry.id))
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>,
                        ) {
                            if (response.isSuccessful) {
                                LossesDao.insert(
                                    Losses(
                                        time = System.currentTimeMillis(),
                                        month = month,
                                        year = year,
                                        money = (expiry.quantity * expiry.sellPrice).toLong()
                                    )
                                ) { b ->
                                    if (b) {
                                        message.postValue("Đã hủy thành công")
                                    }
                                }
                                Log.e(TAG, "body: ${response.body()?.string()}")
                                Log.e(TAG, "errorBody: ${response.errorBody()?.string()}")
                                getListProduct()
                            } else {
                                message.postValue("Máy chủ không phản hồi")
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