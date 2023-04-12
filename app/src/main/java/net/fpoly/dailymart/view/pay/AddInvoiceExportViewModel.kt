package net.fpoly.dailymart.view.pay

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.model.InvoiceType
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.ProductInvoiceParam
import net.fpoly.dailymart.data.model.param.InvoiceParam
import net.fpoly.dailymart.data.repository.ProductRepositoryImpl
import net.fpoly.dailymart.extension.view_extention.hideKeyboard
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.pay.AddInvoiceExportActivity.Companion.TAG_FINAL_INVOICE
import net.fpoly.dailymart.view.payment.PaymentActivity

class AddInvoiceExportViewModel(context: Context) : ViewModel() {

    val showSnackbar = MutableLiveData<String>()
    private val repoProduct = ProductRepositoryImpl()
    private val listProducts = MutableLiveData<List<Product>>()
    private val token = SharedPref.getAccessToken(context)
    private val user = SharedPref.getUser(context)

    val productInBarcode = MutableLiveData<Product?>(null)
    val productBarCode = MutableLiveData<String>()

    private val listProductInvoices = mutableListOf<ProductInvoiceParam>()
    val invoiceDetails = MutableLiveData(mutableListOf<ProductInvoiceParam>())

    var totalInvoice: Long = 0
    private var currentBarcode: String = ""

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
            if (currentBarcode.isNotEmpty()) getInvoiceDetail(currentBarcode)
        }
    }

    fun getInvoiceDetail(barcode: String) {
        currentBarcode = barcode
        listProducts.value?.also { products ->
            val productBarcode = products.filter { it.barcode == barcode }
            productInBarcode.postValue(productBarcode.first())
            productBarCode.postValue(productBarcode.first().barcode)
        }
    }

    fun addInvoiceDetail(context: Context) {
        productInBarcode.value?.also { product ->
            if (product.expires.isEmpty() || product.totalQuantity == 0) {
                showSnackbar.value = OUT_OF_STOCK
                return
            }

            val param = ProductInvoiceParam(
                id = product.id,
                name = product.name,
                unitPrice = product.sellPrice,
                quantity = 1,
                total = product.sellPrice,
                expiryDate = product.expires[0].expiryDate
            )
            val isAlready = listProductInvoices.find { it.id == param.id }
            if (isAlready != null) {
                plusProduct(isAlready)
            } else {
                listProductInvoices.add(param)
            }

            invoiceDetails.value = listProductInvoices
            (context as AddInvoiceExportActivity).setScanBarcodeTrue()
            cancelInvoiceDetail(context)
            context.hideKeyboard()
        }
    }

    fun cancelInvoiceDetail(context: Context) {
        productInBarcode.value = null
        productBarCode.value = ""
        (context as AddInvoiceExportActivity).setScanBarcodeTrue()
        context.hideKeyboard()
    }

    fun onTextQualityProductChange(editText: EditText, product: ProductInvoiceParam) {
        val quantityChange = editText.text.toString().toIntOrNull() ?: return
        val productInList = listProducts.value!!.findLast { it.id == product.id }
        val index = listProductInvoices.indexOf(product)
        productInList?.also {
            if (quantityChange <= productInList.totalQuantity) {
                val new = product.apply {
                    quantity = quantityChange
                    total = quantityChange * unitPrice
                }
                listProductInvoices[index] = new
            } else {
                val new = product.apply {
                    quantity = productInList.totalQuantity
                    total = productInList.totalQuantity * unitPrice
                }
                listProductInvoices[index] = new
                showSnackbar.value = MAXIMUM
            }
            invoiceDetails.postValue(listProductInvoices)
        }
    }

    fun plusProduct(product: ProductInvoiceParam) {
        val index = listProductInvoices.indexOf(product)
        val newQuantity = product.quantity.plus(1)
        val productInList = listProducts.value!!.findLast { it.id == product.id }
        productInList?.also {
            if (newQuantity > it.totalQuantity) return
            listProductInvoices[index] = product.apply {
                quantity = newQuantity
                total = newQuantity * unitPrice
            }
            invoiceDetails.value = listProductInvoices
        }
    }

    fun minusProduct(product: ProductInvoiceParam) {
        val index = listProductInvoices.indexOf(product)
        val newQuantity = product.quantity.minus(1)
        val productInList = listProducts.value!!.findLast { it.id == product.id }
        productInList?.also {
            if (newQuantity < 0) return
            listProductInvoices[index] = product.apply {
                quantity = newQuantity
                total = newQuantity * unitPrice
            }
            invoiceDetails.value = listProductInvoices
        }
    }

    fun btnPaymentClick(context: Context) {
        val productsFinal = listProductInvoices.filter { it.quantity > 0 }
        if (listProductInvoices.isEmpty() || totalInvoice == 0L) {
            showSnackbar.value = NO_HAVE_BUY
            return
        }
        val invoiceParam = InvoiceParam().apply {
            idCustomer = CUSTOMER_ID_DEFAULT
            idUser = user.id
            products = ArrayList(productsFinal)
            invoiceType = InvoiceType.EXPORT.name
            totalBill = totalInvoice
        }
        Intent(context, PaymentActivity::class.java).also {
            it.putExtra(TAG_FINAL_INVOICE, invoiceParam)
            context.startActivity(it)
        }

    }

    companion object {
        const val TAG = "55959595"
        const val OUT_OF_STOCK = "Mặt hàng này đã hết"
        const val MAXIMUM = "Số lượng đã đạt tối đa"
        const val CUSTOMER_ID_DEFAULT = "642d285acf62ee68ba804759"
        const val NO_HAVE_BUY = "Không có hóa đơn nào được tạo"

    }
}