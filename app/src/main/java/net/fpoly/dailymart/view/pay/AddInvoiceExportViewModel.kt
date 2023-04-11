package net.fpoly.dailymart.view.pay

import android.content.Context
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.ProductInvoiceParam
import net.fpoly.dailymart.data.repository.ProductRepositoryImpl
import net.fpoly.dailymart.utils.SharedPref

class AddInvoiceExportViewModel(context: Context) : ViewModel() {

    val showSnackbar = MutableLiveData<String>()
    private val repoProduct = ProductRepositoryImpl()
    private val listProducts = MutableLiveData<List<Product>>()
    val token = SharedPref.getAccessToken(context)

    val productInBarcode = MutableLiveData<Product?>(null)
    val productBarCode = MutableLiveData<String>()

    private val listProductInvoices = mutableListOf<ProductInvoiceParam>()

//    ProductInvoiceParam(
//    id = "6434e5c1a4c07c274fcca130",
//    name = "Play Candy",
//    unitPrice = 12000,
//    quantity = 1,
//    total = 72000,
//    expiryDate = 4133894400000
//    ),
//    ProductInvoiceParam(
//    id = "6434130a47ab55fd626b738a",
//    name = "Phở Gà Cung Đình",
//    unitPrice = 10000,
//    quantity = 1,
//    total = 40000,
//    expiryDate = 4133894400000
//    )

    val invoiceDetails = MutableLiveData(mutableListOf<ProductInvoiceParam>())

    val listProductName = listProducts.switchMap { products ->
        MutableLiveData(products.map { it.barcode })
    }


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
            productBarCode.value = productBarcode.first().barcode
        }
    }

    fun addInvoiceDetail(context: Context) {
        productInBarcode.value?.also { product ->
            if (product.expires.isEmpty() || product.totalQuantity == 0) {
                showSnackbar.value = OUT_OF_STOCK
                return
            }

            val paramInvoiceDetail = ProductInvoiceParam(
                id = product.id,
                name = product.name,
                unitPrice = product.sellPrice,
                quantity = 1,
                total = product.sellPrice,
                expiryDate = product.expires[0].expiryDate
            )
            listProductInvoices.add(paramInvoiceDetail)
            invoiceDetails.value = listProductInvoices
            Log.e(TAG, "addInvoiceDetail: ${invoiceDetails.value}")
            cancelInvoiceDetail(context)
        }
    }

    fun cancelInvoiceDetail(context: Context) {
        productInBarcode.value = null
        productBarCode.value = ""
    }

    fun onTextQualityProductChange(editText: EditText, product: ProductInvoiceParam) {
        val quantityChange = editText.text.toString().toIntOrNull() ?: 0
        val id = product.id
        listProductInvoices.forEachIndexed { index, productInvoice ->
            if (productInvoice.id == id) {
                if(quantityChange <= productInvoice.total) {
                    listProductInvoices[index] = product.apply {
                        quantity = quantityChange
                        total = unitPrice * quantityChange
                    }
                }else {
                    listProductInvoices[index] = product.apply {
                        quantity = productInvoice.quantity
                        total = unitPrice * quantityChange
                    }
                    showSnackbar.value = MAXIMUM
                }
            }
        }
        invoiceDetails.value = listProductInvoices
    }

    companion object {
        const val TAG = "55959595"
        const val OUT_OF_STOCK = "Mặt hàng này đã hết"
        const val MAXIMUM = "Số lượng không đủ"
    }
}