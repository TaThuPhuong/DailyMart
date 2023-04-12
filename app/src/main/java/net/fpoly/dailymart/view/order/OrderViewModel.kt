package net.fpoly.dailymart.view.order

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.repository.ProductRepositoryImpl
import net.fpoly.dailymart.utils.SharedPref

class OrderViewModel(context: Context) : ViewModel() {

    val showSnackbar = MutableLiveData<String>()
    val token = SharedPref.getAccessToken(context)
    private val productRepo = ProductRepositoryImpl()

    val products = MutableLiveData(listOf<Product>())
    val currentProduct = MutableLiveData<Product>()
    init {
        viewModelScope.launch {
            getProducts()
        }
    }

    private suspend fun getProducts() = withContext(Dispatchers.IO) {
        val res = productRepo.getAllProduct(token)
        if (res.isSuccess()) {
            products.postValue(res.data)
        }else {
            showSnackbar.postValue("Lấy danh sách sản phẩm thất bại")
        }
    }

    fun getProduct(barcode: String) {
        products.value.also { list ->
            val product = list?.firstOrNull { it.barcode == barcode}
            product?.let {
                currentProduct.value = it
            }
        }
    }
}
