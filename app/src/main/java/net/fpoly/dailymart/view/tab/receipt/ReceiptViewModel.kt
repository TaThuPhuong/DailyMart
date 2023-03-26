package net.fpoly.dailymart.view.tab.receipt

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.data.model.InvoiceType
import net.fpoly.dailymart.repository.InvoiceRepository
import net.fpoly.dailymart.utils.SharedPref

class ReceiptViewModel(
    context: Context,
    private val repository: InvoiceRepository
) : ViewModel() {

    private val _openTabReceipt = MutableLiveData(TAB_SELL)
    val openTabReceipt: LiveData<Int> = _openTabReceipt
    private val user = SharedPref.getUser(context)

    var invoicesResult: MutableList<Invoice> = mutableListOf()
    val invoices = MutableLiveData<MutableList<Invoice>>()


    val invoiceImport = invoices.switchMap { filterInvoice(it, InvoiceType.IMPORT) }
    val invoiceSell = invoices.switchMap { filterInvoice(it, InvoiceType.SELL) }
    val invoiceDeduction = invoices.switchMap { filterInvoice(it, InvoiceType.DEDUCTION) }

    init {
        viewModelScope.launch {
            val invoices = repository.getInvoices()
            invoices.onSuccess {
                val result = it.toMutableList()
                invoicesResult = result
                this@ReceiptViewModel.invoices.value = result
            }
            invoices.onFailure {
                this@ReceiptViewModel.invoices.value = mutableListOf()
            }
        }
    }

    private fun filterInvoice(
        invoices: MutableList<Invoice>,
        typeInvoice: InvoiceType
    ): LiveData<MutableList<Invoice>> {
        val invoicesImport = invoices.filter { it.invoiceType == typeInvoice.name }.toMutableList()
        return MutableLiveData(invoicesImport)
    }


    fun onOpenTab(id: Int) {
        _openTabReceipt.value = id
    }

    fun detailInvoice(context: Context, invoice: Invoice){

    }

    fun addNewInvoice(context: Context) {
        Toast.makeText(context, "ReceiptViewModel", Toast.LENGTH_LONG).show()
//        val invoice = Invoice(userId = user.id, invoiceType = InvoiceType.IMPORT.name)
//        viewModelScope.launch {
//            repository.insertInvoice(invoice)
//            Log.e(TAG, "THÊM THÀNH CÔNG")
//            _invoices.value?.add(invoice)
//        }
    }

    companion object {
        const val TAG = "ReceiptViewModel"
        const val TAB_SELL = 1
        const val TAB_IMPORT = 2
        const val TAB_DEDUCTION = 3
    }
}