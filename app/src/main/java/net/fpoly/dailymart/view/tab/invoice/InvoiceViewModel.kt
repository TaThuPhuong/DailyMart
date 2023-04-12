package net.fpoly.dailymart.view.tab.invoice

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.data.model.InvoiceType
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.repository.InvoiceRepositoryImpl
import net.fpoly.dailymart.repository.InvoiceRepository
import net.fpoly.dailymart.utils.SharedPref

class InvoiceViewModel(context: Context) : ViewModel() {

    private val repoInvoice: InvoiceRepository = InvoiceRepositoryImpl()

    private val _openTabReceipt = MutableLiveData(TAB_EXPORT)
    val openTabReceipt: LiveData<Int> = _openTabReceipt
    private val token = SharedPref.getAccessToken(context)

    var invoicesResult: MutableList<Invoice> = mutableListOf()
    val invoices = MutableLiveData(mutableListOf<Invoice>())

    val invoiceImport = invoices.switchMap { filterInvoice(it, InvoiceType.IMPORT) }
    val invoiceSell = invoices.switchMap { filterInvoice(it, InvoiceType.EXPORT) }
    val invoiceDeduction = invoices.switchMap { filterInvoice(it, InvoiceType.REFUND) }

    val showSnackbar = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val isShowEmptyList = MutableLiveData(false)
    val isRefund = MutableLiveData(false)

    private lateinit var invoiceRefund : Invoice

    init {
        viewModelScope.launch { getAllInvoice() }
    }

    private suspend fun getAllInvoice() {
        isLoading.postValue(true)
        when (val res = repoInvoice.getInvoices(token)) {
            is Response.Success -> {
                invoicesResult = res.data
                invoices.postValue(res.data)
            }
            is Response.Error -> {
                showSnackbar.value = res.message
            }
        }
        isLoading.postValue(false)
    }

    private fun filterInvoice(
        invoices: MutableList<Invoice>,
        typeInvoice: InvoiceType
    ): LiveData<MutableList<Invoice>> {
        val invoicesImport =
            invoices.filter { it.type == typeInvoice.name }
                .toMutableList()
        return MutableLiveData(invoicesImport)
    }


    fun onOpenTab(id: Int) {
        _openTabReceipt.value = id
        isShowEmptyList()
    }

    fun detailInvoice(context: Context, invoice: Invoice) {
        DetailInvoiceDialog(context, this, invoice).show()
    }

    private fun isShowEmptyList(): Boolean {
        val isShow = when {
            openTabReceipt.value == TAB_EXPORT && invoiceSell.value.isNullOrEmpty() -> true
            openTabReceipt.value == TAB_IMPORT && invoiceImport.value.isNullOrEmpty() -> true
            openTabReceipt.value == TAB_REFUND && invoiceDeduction.value.isNullOrEmpty() -> true
            else -> false
        }
        isShowEmptyList.value = isShow
        return isShow
    }

    fun refundInvoice(invoice: Invoice){
        isRefund.value = true
        invoiceRefund = invoice
    }

    companion object {
        const val TAG = "ReceiptViewModel"
        const val TAB_EXPORT = 1
        const val TAB_IMPORT = 2
        const val TAB_REFUND = 3
    }
}