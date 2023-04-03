package net.fpoly.dailymart.view.supplier

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.data.model.SupplierParam
import net.fpoly.dailymart.repository.SupplierRepository

class SupplierViewModel(val repository: SupplierRepository) : ViewModel() {

    val listSupplier = MutableLiveData<List<Supplier>>()
    var listSupplierRemote: ArrayList<Supplier> = arrayListOf()

    val eventShowDialogAdd = MutableLiveData<Boolean>()
    val showSnackbar = MutableLiveData<String>()

    init {
        getAllSuppliers()
    }

    private fun getAllSuppliers() {
        viewModelScope.launch(Dispatchers.IO) {
            val resultData = repository.getSuppliers(TOKEN_TEST)
            resultData.result.also {
                listSupplierRemote = it
                listSupplier.postValue(it)
            }
        }
    }

    fun callToSupplier(context: Context, supplier: Supplier) {
        val phoneNumber = supplier.phoneNumber
        val phoneUri = Uri.parse("tel:$phoneNumber")
        val phoneIntent = Intent(Intent.ACTION_DIAL, phoneUri)
        context.startActivity(phoneIntent)
    }

    fun messageToSupplier(context: Context, supplier: Supplier) {
        val phoneNumber = supplier.phoneNumber
        val smsUri = Uri.parse("sms:$phoneNumber")
        context.startActivity(Intent(Intent.ACTION_VIEW, smsUri))
    }

    fun showDialogAdd() {
        eventShowDialogAdd.value = true
    }

    fun addNewSupplier(supplier: SupplierParam) {
        viewModelScope.launch {
            val result = repository.insertSupplier(supplier, TOKEN_TEST)
            if (result.isSuccess()) {
                showSnackbar.postValue(MESSAGE_ADD_SUCCESS)
            } else {
                showSnackbar.postValue(MESSAGE_ADD_FAILED)
            }
        }
    }

    companion object {
        const val TAG = "BXT"
        const val TOKEN_TEST =
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoic3RhZmYiLCJpYXQiOjE2ODA1MjgyNjIsImV4cCI6MTY4MDYxNDY2Mn0.o7TYPKY3P4eHt_EK8oxg0USJ0FrtbtB0FwURYWjq0e4"
        const val MESSAGE_ADD_SUCCESS = "Thêm thành công"
        const val MESSAGE_ADD_FAILED = "Thêm thất bại"
    }
}

//    fun insertSuppliers(supplier: Supplier) {
//        viewModelScope.launch {
//            supplierRepository.insertSupplier(supplier)
//        }
//    }
//    fun deleteSuppliers(supplier: Supplier) {
//        viewModelScope.launch {
//            supplierRepository.deleteSupplier(supplier)
//        }
//    }
//}

