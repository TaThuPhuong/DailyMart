package net.fpoly.dailymart.view.stock

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.repository.ProductRepository
import net.fpoly.dailymart.utils.SharedPref

class StockViewModel(private val app: Application, private val productRepo: ProductRepository) :
    ViewModel() {

    private val TAG = "YingMing"
    private val mToken = SharedPref.getAccessToken(app)
    private val _listProduct = MutableLiveData<List<Product>>(ArrayList())
    val listProducts: LiveData<List<Product>> = _listProduct
    val getProductSuccess = MutableLiveData(false)

    val message = MutableLiveData("")
    fun getListProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = productRepo.getAllProduct(mToken)
            if (res.isSuccess()) {
                _listProduct.postValue(res.data!!.filter { it.status })
                getProductSuccess.postValue(true)
            } else {
                message.postValue("Máy chủ không phản hồi")
                getProductSuccess.postValue(true)
            }
        }
    }
}