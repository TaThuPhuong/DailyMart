package net.fpoly.dailymart.view.pay

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.ProductInvoiceParam
import net.fpoly.dailymart.data.model.ProductParam
import net.fpoly.dailymart.data.repository.ProductRepositoryImpl
import net.fpoly.dailymart.repository.ProductRepository
import net.fpoly.dailymart.utils.SharedPref
import kotlin.math.log

class AddInvoiceExportViewModel(context: Context) : ViewModel() {

    val showSnackbar = MutableLiveData<String>()
    val repoProduct = ProductRepositoryImpl()
    val listProducts = MutableLiveData<List<Product>>()
    val token = SharedPref.getAccessToken(context)


    val productInBarcode = MutableLiveData<Product?>(null)
    val productBarCodeQuantity = MutableLiveData<String>()

    val listInvoiceDetail = mutableListOf<ProductInvoiceParam>()

    init {
        viewModelScope.launch {
            getAllProduct()
        }
    }

    private suspend fun getAllProduct() = withContext(Dispatchers.IO) {
        val res = repoProduct.getAllProduct(token)
        Log.e(TAG, "getAllProduct: ${res.data}  --- ${res.message} --- ${res.status}")

        if (res.isSuccess() && res.data != null) {
            listProducts.postValue(res.data!!)
        }
    }

    fun getInvoiceDetail(barcode: String) {
        listProducts.value?.also { products ->
            val productBarcode = products.filter { it.barcode == barcode }
            productInBarcode.value = productBarcode.first()
            productBarCodeQuantity.value = "1"
        }
    }

    companion object {
        const val TAG = "55959595"
    }
}