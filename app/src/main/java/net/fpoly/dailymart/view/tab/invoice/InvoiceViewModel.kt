package net.fpoly.dailymart.view.tab.invoice

import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.data.model.InvoiceType
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.repository.InvoiceRepositoryImpl
import net.fpoly.dailymart.repository.InvoiceRepository
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.detailinvoice.DetailInvoiceActivity
import net.fpoly.dailymart.view.detailinvoice.DetailInvoiceActivity.Companion.INVOICE

class InvoiceViewModel(context: Context) : ViewModel() {

    private val repoInvoice: InvoiceRepository = InvoiceRepositoryImpl()

    private val _openTabReceipt = MutableLiveData(TAB_EXPORT)
    val openTabReceipt: LiveData<Int> = _openTabReceipt
    private val token = SharedPref.getAccessToken(context)

    var invoicesResult: MutableList<Invoice> = mutableListOf()
    val invoices = MutableLiveData(mutableListOf<Invoice>())

    val invoiceImport = invoices.switchMap { filterInvoice(it, InvoiceType.IMPORT) }
    val invoiceSell =
        invoices.switchMap { filterInvoice(it, InvoiceType.REFUND, InvoiceType.EXPORT) }

    val showSnackbar = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val isRefund = MutableLiveData(false)
    val isShowEmptyList = MutableLiveData(false)

    private lateinit var invoiceRefund: Invoice

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
        delay(200)
        checkShowEmptyList()
    }

    private fun filterInvoice(
        invoices: MutableList<Invoice>,
        vararg typeInvoice: InvoiceType
    ): LiveData<MutableList<Invoice>> {
        val invoicesImport =
            invoices.filter { list -> typeInvoice.any { list.type == it.name } }
                .toMutableList()
        return MutableLiveData(invoicesImport)
    }

    fun onOpenTab(id: Int) {
        _openTabReceipt.value = id
        if(isLoading.value == false) {
            checkShowEmptyList()
        }
    }

    fun detailInvoice(context: Context, invoice: Invoice) {
        Intent(context, DetailInvoiceActivity::class.java).also {
            it.putExtra(INVOICE, invoice)
            context.startActivity(it)
        }
    }

    fun checkShowEmptyList(): Boolean {
        val isShow = when {
            openTabReceipt.value == TAB_EXPORT && invoiceSell.value.isNullOrEmpty() -> true
            openTabReceipt.value == TAB_IMPORT && invoiceImport.value.isNullOrEmpty() -> true
            else -> false
        }
        isShowEmptyList.value = isShow
        return isShow
    }

    companion object {
        const val TAG = "ReceiptViewModel"
        const val TAB_EXPORT = 1
        const val TAB_IMPORT = 2
    }
}