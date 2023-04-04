package net.fpoly.dailymart.view.supplier

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.data.model.SupplierParam
import net.fpoly.dailymart.repository.SupplierRepository
import net.fpoly.dailymart.utils.SharedPref

class SupplierViewModel(context: Context ,val repository: SupplierRepository) : ViewModel() {

    val listSupplier = MutableLiveData<List<Supplier>>()
    var listSupplierRemote: ArrayList<Supplier> = arrayListOf()
    var isLoading = MutableLiveData(false)

    val showSnackbar = MutableLiveData<String>()
    val token = SharedPref.getAccessToken(context)

    init {
        getAllSuppliers()
    }

    private fun getAllSuppliers() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            val resultData = repository.getSuppliers(token)
            resultData.result.also {
                listSupplierRemote = it
                listSupplier.postValue(it)
            }
            isLoading.postValue(false)
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

    fun addNewSupplier(supplier: SupplierParam) {
        viewModelScope.launch {
            val result = repository.insertSupplier(supplier, token)
            if (result.isSuccess()) {
                getAllSuppliers()
                showSnackbar.postValue(MESSAGE_ADD_SUCCESS)
            } else {
                showSnackbar.postValue(MESSAGE_ADD_FAILED)
            }
        }
    }

    fun editNewSupplier(id: String, supplier: SupplierParam) {
        viewModelScope.launch {
            val result = repository.editSupplier(id, supplier, token)
            if (result.isSuccess()) {
                getAllSuppliers()
                showSnackbar.postValue(MESSAGE_EDIT_SUCCESS)
            } else {
                showSnackbar.postValue(MESSAGE_EDIT_FAILED)
            }
        }
    }

    fun removeSupplier(supplier: Supplier) {
        viewModelScope.launch {
            val res = repository.removeSupplier(supplier, token)
            if (res.isSuccess()) {
                getAllSuppliers()
                showSnackbar.postValue(MESSAGE_REMOVE_SUCCESS)
            } else {
                showSnackbar.postValue(MESSAGE_REMOVE_FAILED)
            }
        }
    }

    fun showDialogAdd(context: Context){
        AddEditSupplierDialog(context, null, this).show()
    }

    fun clickItems(context: Context, supplier: Supplier) {
        MoreSupplierPopup(context, supplier, this).show()
    }

    fun openRemoveSupplier(context: Context, supplier: Supplier) {
        ConfirmRemoveDialog(context, supplier, this).show()
    }

    fun openEditSupplier(context: Context, supplier: Supplier) {
        AddEditSupplierDialog(context, supplier, this).show()
    }

    companion object {
        const val TAG = "BXT"
        const val MESSAGE_ADD_SUCCESS = "Thêm thành công"
        const val MESSAGE_ADD_FAILED = "Thêm thất bại"
        const val MESSAGE_REMOVE_SUCCESS = "Xóa thành công"
        const val MESSAGE_REMOVE_FAILED = "Xóa thất bại"
        const val MESSAGE_EDIT_SUCCESS = "Sửa thành công"
        const val MESSAGE_EDIT_FAILED = "Sửa thất bại"

    }
}

