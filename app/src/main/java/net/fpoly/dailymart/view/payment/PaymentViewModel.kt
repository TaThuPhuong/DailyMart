package net.fpoly.dailymart.view.payment

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.R
import net.fpoly.dailymart.data.model.Customer
import net.fpoly.dailymart.data.model.CustomerParam
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.model.param.InvoiceParam
import net.fpoly.dailymart.data.repository.CustomerRepositoryImpl
import net.fpoly.dailymart.data.repository.InvoiceRepositoryImpl
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.pay.AddInvoiceExportActivity
import java.net.URLEncoder

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

    fun sendInvoice(context: Context) {
        val invoice = invoice.value ?: return
        val textToEncode = Gson().toJson(invoice)
        val string = URLEncoder.encode(textToEncode, "UTF-8")
        val width = 500
        val height = 500
        try {
            val writer = QRCodeWriter()
            val bitMatrix: BitMatrix = writer.encode(string, BarcodeFormat.QR_CODE, 512, 512)
            val bmp: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }
            val dialog = Dialog(context, R.style.BaseThemeDialog)
            dialog.setContentView(R.layout.layout_image_dialog)
            dialog.findViewById<ImageView>(R.id.imageQR).setImageBitmap(bmp)
            dialog.show()
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    fun createInvoice() {
        viewModelScope.launch {
            invoice.value?.also {
                isLoading.postValue(true)
                when (val res = invoiceRepo.insertInvoice(token, it)) {
                    is Response.Success -> {
                        showSnackbar.postValue(MESSAGE_SUCCESS_INVOICE)
                        eventCreateInvoiceSuccess.value = Unit
                    }
                    is Response.Error -> {
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