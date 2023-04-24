package net.fpoly.dailymart.view.order

import android.content.Context
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.model.InvoiceType
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.ProductInvoiceParam
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.model.param.InvoiceParam
import net.fpoly.dailymart.data.repository.InvoiceRepositoryImpl
import net.fpoly.dailymart.data.repository.ProductRepositoryImpl
import net.fpoly.dailymart.utils.SharedPref
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderViewModel(context: Context) : ViewModel() {

    val showSnackbar = MutableLiveData<String>()
    val token = SharedPref.getAccessToken(context)
    val user = SharedPref.getUser(context)

    private val invoiceRepo = InvoiceRepositoryImpl()
    private val productRepo = ProductRepositoryImpl()

    val products = MutableLiveData<List<Product>>()
    val currentProduct = MutableLiveData<Product>()

    val invoiceProducts = MutableLiveData<ArrayList<ProductInvoiceParam>>()
    private val listProductInvoices = arrayListOf<ProductInvoiceParam>()

    val isEdit = MutableLiveData(false)
    val isShowLoading = MutableLiveData(false)
    val eventDone = MutableLiveData<Unit>()

    init {
        viewModelScope.launch {
            getProducts()
        }
    }

    private suspend fun getProducts() = withContext(Dispatchers.IO) {
        isShowLoading.postValue(true)
        val res = productRepo.getAllProduct(token)
        if (res.isSuccess()) {
            val filter = res.data?.filter { it.status == true } ?: return@withContext
            this@OrderViewModel.products.postValue(filter)
        } else {
            showSnackbar.postValue("Lấy danh sách sản phẩm thất bại")
        }
        isShowLoading.postValue(false)
    }

    fun getProduct(barcode: String) {
        products.value.also { list ->
            val product = list?.firstOrNull { it.barcode == barcode } ?: return
            currentProduct.postValue(product)
        }
    }

    fun addNewProductInvoice(
        edBarcode: EditText,
        edName: EditText,
        edQuantity: EditText,
        edExpiry: EditText,
        edUnitPrice: EditText
    ) {
        if (edBarcode.text.toString().isEmpty()) return
        val productId =
            products.value?.firstOrNull { it.barcode == edBarcode.text.toString() } ?: return
        val unitPrice = edUnitPrice.text.toString().toLongOrNull() ?: return
        val quantity = edQuantity.text.toString().toIntOrNull() ?: return
        val expiryDate = edExpiry.text.toString().convertDateToMilliseconds() ?: return
        val nowTime = Calendar.getInstance()
        nowTime.set(
            nowTime[Calendar.YEAR],
            nowTime[Calendar.MONTH] + 1,
            nowTime[Calendar.DAY_OF_MONTH]
        )


        if (unitPrice < 0) {
            edUnitPrice.error = "Đơn giá không hợp lệ"
            return
        }
        if (edExpiry.text.toString().isEmpty() || expiryDate <= nowTime.timeInMillis) {
            edExpiry.error = "Hạn sử dụng không hợp lệ"
            return
        }

        val total = unitPrice * quantity

        val product = ProductInvoiceParam(
            name = edName.text.toString(),
            quantity = quantity,
            expiryDate = expiryDate,
            unitPrice = unitPrice,
            total = total,
            id = productId.id
        )

        val productFound =
            listProductInvoices.filter {
                it.id == product.id
                        && it.expiryDate == product.expiryDate
                        && it.unitPrice == product.unitPrice
            }
        if (productFound.isNotEmpty()) {
            val new = productFound.first().apply {
                this.quantity += quantity
                this.total = this.quantity * unitPrice
            }
            val position = listProductInvoices.indexOf(productFound.first())
            listProductInvoices[position] = new
            invoiceProducts.value = listProductInvoices
            return
        }

        listProductInvoices.add(product)
        invoiceProducts.value = listProductInvoices

        edBarcode.setText("")
        edName.setText("")
        edQuantity.setText("")
        edExpiry.setText("")
        edUnitPrice.setText("")
        edQuantity.error = null
        edExpiry.error = null
    }

    private fun String.convertDateToMilliseconds(): Long? {
        if (this.isEmpty()) return null
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = dateFormat.parse(this) ?: return 0
        return date.time
    }

    fun removeProduct(pos: Int) {
        listProductInvoices.removeAt(pos)
        invoiceProducts.value = listProductInvoices
    }

    fun editProduct() {
        isEdit.value?.also {
            isEdit.value = !it
        }
    }

    fun paymentClick() {
        viewModelScope.launch {
            val filter = ArrayList(listProductInvoices.filter { it.total > 0 })
            if (filter.isEmpty()) {
                showSnackbar.value = "Chưa có sản phẩm nhập"
                return@launch
            }
            isShowLoading.postValue(true)
            val invoiceParam = InvoiceParam(
                idUser = user.id,
                invoiceType = InvoiceType.IMPORT.name,
                products = filter,
                totalBill = listProductInvoices.sumOf { it.total }
            )
            Log.e(TAG, "paymentClick: ${Gson().toJson(invoiceParam)}")
            when (val res = invoiceRepo.insertInvoice(token, invoiceParam)) {
                is Response.Success -> {
                    showSnackbar.postValue("Tạo mới hóa đơn thành công")
                    eventDone.postValue(Unit)
                }

                is Response.Error -> {
                    showSnackbar.postValue(res.message)
                }
            }
            isShowLoading.postValue(false)
        }
    }

    companion object {
        const val TAG = "Senior"
    }
}
