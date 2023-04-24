package net.fpoly.dailymart.view.tab.invoice

import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.data.model.InvoiceType
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.repository.InvoiceRepositoryImpl
import net.fpoly.dailymart.repository.InvoiceRepository
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.detailinvoice.DetailInvoiceActivity
import net.fpoly.dailymart.view.detailinvoice.DetailInvoiceActivity.Companion.INVOICE

class InvoiceViewModel(context: Context) : ViewModel() {

    private val repoInvoice: InvoiceRepository = InvoiceRepositoryImpl()

    private val _openTabReceipt = MutableLiveData(TAB_EXPORT)
    val openTabReceipt: LiveData<Int> = _openTabReceipt
    private val token = SharedPref.getAccessToken(context)
    val user = SharedPref.getUser(context)

    var rootInvoices = mutableListOf<Invoice>()
    var listInvoice = mutableListOf<Invoice>()
    val invoices = MutableLiveData(mutableListOf<Invoice>())

    val showSnackbar = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val isShowEmptyList = invoices.switchMap {
        MutableLiveData(it.isNullOrEmpty())
    }
    var nowPage = 1
    var totalPage = 1

    init {
        viewModelScope.launch { getInvoices() }
    }

    private suspend fun getInvoices() {
        isLoading.postValue(true)
        when (val res = repoInvoice.getInvoicesPage(token, nowPage)) {
            is Response.Success -> {
                val root = res.data.toMutableList()
                rootInvoices.addAll(root)
                nowPage++
                totalPage = res.page
                listInvoice = rootInvoices
                showInvoice()
            }

            is Response.Error -> {
                showSnackbar.value = res.message
            }
        }
        isLoading.postValue(false)
    }

    fun loadMore() {
        if (nowPage >= totalPage) return
        viewModelScope.launch {
            val res = repoInvoice.getInvoicesPage(token, nowPage)
            if (res is Response.Success) {
                val root = res.data.toMutableList()
                rootInvoices.addAll(root)
                nowPage++
                totalPage = res.page
                listInvoice = rootInvoices
                showInvoice()
            }
        }
    }

    fun showInvoice(tab: Int? = _openTabReceipt.value) {
        val filter = when (tab) {
            TAB_EXPORT -> listInvoice.filter { it.type == InvoiceType.EXPORT.name || it.type == InvoiceType.REFUND.name }
                .toMutableList()

            TAB_IMPORT -> listInvoice.filter { it.type == InvoiceType.IMPORT.name }.toMutableList()
            else -> mutableListOf()
        }

        invoices.postValue(filter)
        if (filter.size <= 10) loadMore()
    }

    fun isStaff(invoice: Invoice): Boolean =
        user.role == ROLE.staff && invoice.type == InvoiceType.IMPORT.name

    fun onOpenTab(id: Int) {
        viewModelScope.launch {
            _openTabReceipt.postValue(id)
            showInvoice(id)
        }
    }

    fun detailInvoice(context: Context, invoice: Invoice) {
        Intent(context, DetailInvoiceActivity::class.java).also {
            it.putExtra(INVOICE, invoice)
            context.startActivity(it)
        }
    }

    companion object {
        const val TAG = "ReceiptViewModel"
        const val TAB_EXPORT = 1
        const val TAB_IMPORT = 2
    }
}