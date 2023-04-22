package net.fpoly.dailymart.view.detailinvoice

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.data.model.param.InvoiceParam
import net.fpoly.dailymart.data.repository.InvoiceRepositoryImpl
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.main.MainActivity
import net.fpoly.dailymart.view.payment.PaymentActivity
import kotlin.time.Duration.Companion.days

class DetailInvoiceViewModel(context: Context) : ViewModel() {

    val token = SharedPref.getAccessToken(context)
    val user = SharedPref.getUser(context)
    private val invoiceRepo = InvoiceRepositoryImpl()

    val showSnackbar = MutableLiveData<String>()
    val invoice = MutableLiveData<Invoice>()
    val isRefund = MutableLiveData(false)
    val invoiceParam = MutableLiveData<InvoiceParam>()
    private lateinit var invoiceRoot: InvoiceParam
    private lateinit var rootInvoice: Invoice

    private var rootProducts = mutableListOf<ProductInvoiceParam>()
    private var rootProductsRefund = mutableListOf<ProductInvoiceParam>()
    private var changeProducts = mutableListOf<ProductInvoiceParam>()
    var totalInvoice = MutableLiveData<Long>()
    var isShowRefund = MutableLiveData(false)
    var isShowLoading = MutableLiveData(false)

    fun refundInvoice() {
        val timeInvoice = invoice.value?.createAt ?: return
        val now = System.currentTimeMillis()
        val oneDay = 1.days.inWholeMilliseconds

        if (now - timeInvoice > oneDay) {
            showSnackbar.value = "Đơn hàng này đã quá thời gian hoàn tiền"
            return
        }

        isRefund.value?.also { isRefund ->
            if (!isRefund) {
                changeProducts.removeAll(rootProductsRefund)
                invoiceParam.value?.also {
                    it.products = ArrayList(changeProducts)
                    getTotalInvoice(it)
                    invoiceParam.postValue(it)
                }
            }else {
                invoiceParam.postValue(rootInvoice.toParam())
            }
            this.isRefund.value = !isRefund
        }
    }

    fun getInvoice(activity: DetailInvoiceActivity) {
        with(activity) {
            val invoiceIntent = intent.getParcelableExtra<Invoice>(DetailInvoiceActivity.INVOICE)
            invoiceIntent?.also { invoice1 ->
                rootInvoice = invoice1
                val param = invoice1.toParam()
                invoiceRoot = param
                invoice.value = invoice1
                invoiceParam.value = param
                totalInvoice.value = invoice1.totalBill
                rootProducts = param.products
                changeProducts = param.products.filter { it.quantity > 0 }.toMutableList()
                rootProductsRefund = param.products.filter { it.quantity < 0 }.toMutableList()

                checkShowBtnRefund(invoice1)
                Log.e(TAG, "getInvoice: ${Gson().toJson(invoice1)} --- ${Gson().toJson(param)}")
            }
        }
    }

    private fun checkShowBtnRefund(invoice1: Invoice) {
        isShowRefund.value = invoice1.type != InvoiceType.IMPORT.name && user.role == ROLE.manager
    }

    fun productClick(product: ProductInvoiceParam) {
        Log.e(TAG, "productClick: ${Gson().toJson(product)}")
        if (isRefund.value != true) return
        val isBuyItemClick = product.quantity > 0
        if (isBuyItemClick) {
            plusToRefund(product)
        } else {
            plusToBuy(product)
        }
    }

    private fun plusToBuy(product: ProductInvoiceParam) {
        val productIndex = changeProducts.indexOfFirst { product.id == it.id && it.quantity > 0 }
        val rootProduct = changeProducts.indexOf(product)

        if (rootProduct == -1) return
        if (productIndex < 0) {
            val productChange = ProductInvoiceParam(
                id = product.id,
                name = product.name,
                unitPrice = product.unitPrice,
                quantity = 1,
                expiryDate = product.expiryDate,
                total = 1 * product.unitPrice
            )
            changeProducts.add(productChange)

        } else {
            changeProducts[productIndex].quantity += 1
            changeProducts[productIndex].total =
                changeProducts[productIndex].unitPrice * changeProducts[productIndex].quantity
        }

        val change = changeProducts[rootProduct].apply {
            quantity += 1
            total = quantity * unitPrice
        }

        changeProducts[rootProduct] = change

        invoiceParam.value?.also {
            it.products = ArrayList(changeProducts)
            getTotalInvoice(it)
            invoiceParam.postValue(it)
        }
    }

    private fun plusToRefund(product: ProductInvoiceParam) {
        val productIndex = changeProducts.indexOfFirst { product.id == it.id && it.quantity < 0 }
        val rootProduct = changeProducts.indexOf(product)

        if (productIndex < 0) {
            val productChange = ProductInvoiceParam(
                id = product.id,
                name = product.name,
                unitPrice = product.unitPrice,
                quantity = -1,
                expiryDate = product.expiryDate,
                total = -1 * product.unitPrice
            )
            changeProducts.add(productChange)
        } else {
            changeProducts[productIndex].quantity -= 1
            changeProducts[productIndex].total =
                changeProducts[productIndex].unitPrice * changeProducts[productIndex].quantity
        }

        val change = changeProducts[rootProduct].apply {
            quantity -= 1
            total = quantity * unitPrice
        }

        changeProducts[rootProduct] = change

        invoiceParam.value?.also {
            it.products = ArrayList(changeProducts)
            getTotalInvoice(it)
            invoiceParam.postValue(it)
        }
    }

    private fun getTotalInvoice(invoiceParam: InvoiceParam) {
        val total = invoiceParam.products.filter { it.quantity > 0 }.sumOf { it.total }
        totalInvoice.value = total.toLong()
    }

    fun confirmRefund(context: Context) {
        getFinalInvoice(context)
    }

    private fun getFinalInvoice(context: Context) {
        viewModelScope.launch {
            isShowLoading.postValue(true)
            val changeProduct = changeProducts.filter { it.quantity < 0 }.toMutableList()
//            changeProduct.addAll(rootProductsRefund)

            val final = InvoiceRefund(
                idUser = invoiceRoot.idUser,
                id = invoice.value!!.id,
                products = ArrayList(changeProduct),
                total = totalInvoice.value ?: 0L
            )
            Log.e(TAG, "getFinalInvoice: ${Gson().toJson(final)}")
            when (val res = invoiceRepo.refundInvoice(token, final)) {
                is Response.Success -> starInvoiceNewLoad(context)
                is Response.Error -> showSnackbar.postValue(res.message)
            }
            isShowLoading.postValue(false)
        }

    }

    private fun starInvoiceNewLoad(context: Context) {
        Intent(context, MainActivity::class.java).also {
            it.putExtra(MainActivity.MAIN_EVENT, PaymentActivity.NEW_INVOICE_CREATE)
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(it)
        }
    }

    fun removeInvoice(context: Context) {
        val timeInvoice = invoice.value?.createAt ?: return
        val now = System.currentTimeMillis()
        val oneDay = 1.days.inWholeMilliseconds

        if (now - timeInvoice > oneDay) {
            showSnackbar.value = "Đơn hàng này đã quá thời gian xóa"
            return
        }

        viewModelScope.launch {
            isShowLoading.postValue(true)
            val idInvoice = invoice.value?.id ?: return@launch
            when (val res = invoiceRepo.deleteInvoice(token, idInvoice)) {
                is Response.Success -> starInvoiceNewLoad(context)
                is Response.Error -> showSnackbar.postValue(res.message)
            }
            isShowLoading.postValue(false)
        }
    }

    companion object {
        const val TAG = "Senior"
    }
}