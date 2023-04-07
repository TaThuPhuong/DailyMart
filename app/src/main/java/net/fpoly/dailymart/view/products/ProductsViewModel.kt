package net.fpoly.dailymart.view.products

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.repository.ProductRepository
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class ProductsViewModel(val app: Application, private val repo: ProductRepository) : ViewModel() {

    private val _listProduct = MutableLiveData<List<Product>>(ArrayList())
    val listProduct: LiveData<List<Product>> = _listProduct
    val message = MutableLiveData<String>(null)
    private val _role = MutableLiveData(false)
    val role: LiveData<Boolean> = _role
    val mUser = SharedPref.getUser(app)

    private val mToken = SharedPref.getAccessToken(app)
    init {
        _role.value = mUser!!.role != ROLE.staff
    }
    fun getListProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getAllProduct(mToken)
            if (res.isSuccess()) {
                _listProduct.postValue(res.data!!)
                message.postValue(res.message!!)
            } else {
                message.postValue(res.message!!)
            }
        }
    }
}