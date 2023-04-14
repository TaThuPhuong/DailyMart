package net.fpoly.dailymart.view.payment

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.model.Customer
import net.fpoly.dailymart.data.model.CustomerParam
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.model.param.InvoiceParam
import net.fpoly.dailymart.data.repository.CustomerRepositoryImpl
import net.fpoly.dailymart.data.repository.InvoiceRepositoryImpl
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.pay.AddInvoiceExportActivity

class PaymentViewModel(context: Context) : ViewModel() {

    val invoice = MutableLiveData<InvoiceParam>()
    val showSnackbar = MutableLiveData<String>()

    val token = SharedPref.getAccessToken(context)
    private val customerRepo = CustomerRepositoryImpl()
    private val invoiceRepo = InvoiceRepositoryImpl()

    val customers = MutableLiveData(listOf<Customer>())
    var customerSelected = MutableLiveData<Customer>()
    val eventCreateInvoiceSuccess = MutableLiveData<Unit>()
    val isLoading = MutableLiveData(false)

    init {
        viewModelScope.launch {
            getCustomer()
        }
    }

    private suspend fun getCustomer() = withContext(Dispatchers.IO) {
        when (val res = customerRepo.getCustomers(token)) {
            is Response.Success -> {
                customerSelected(res.data.last())
                customers.postValue(res.data)
            }
            is Response.Error -> {
                showSnackbar.postValue(res.message)
            }
        }
    }

    fun getInvoice(activity: PaymentActivity) {
        with(activity) {
            val formIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(
                    AddInvoiceExportActivity.TAG_FINAL_INVOICE,
                    InvoiceParam::class.java
                )
            } else {
                intent.getParcelableExtra(AddInvoiceExportActivity.TAG_FINAL_INVOICE)
            }
            formIntent?.also {
                invoice.value = it
            }
        }
    }

    fun customerSelected(customer: Customer) {
        customerSelected.postValue(customer)
        invoice.value?.idCustomer = customer.id
    }

    fun createCustomer(param: CustomerParam, callback: () -> Unit) {
        viewModelScope.launch {
            when (val res = customerRepo.addCustomer(token, param)) {
                is Response.Success -> {
                    getCustomer()
                    showSnackbar.postValue(MESSAGE_SUCCESS)
                    callback()
                }
                is Response.Error -> showSnackbar.postValue(res.message)
            }
        }
    }

    fun createInvoice() {
        viewModelScope.launch {
            invoice.value?.also {
                isLoading.postValue(true)
                when (val res = invoiceRepo.insertInvoice(token, it)) {
                    is Response.Success -> {
                        Log.e(TAG, "createInvoice1: $res")
                        showSnackbar.postValue(MESSAGE_SUCCESS_INVOICE)
                        eventCreateInvoiceSuccess.value = Unit
                    }
                    is Response.Error -> {
                        Log.e(TAG, "createInvoice2: $res")
                        showSnackbar.postValue(res.message)
                    }
                }
                isLoading.postValue(false)
            }
        }
    }

    companion object {
        const val TAG = "1000"
        const val MESSAGE_SUCCESS = "Thêm mới thành công"
        const val MESSAGE_SUCCESS_INVOICE = "Tạo hóa đơn thành công"

    }

}