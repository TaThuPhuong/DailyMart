package net.fpoly.dailymart.view.supplier

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.CategoryParam
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.data.model.SupplierParam
import net.fpoly.dailymart.data.model.SupplierParamAdd
import net.fpoly.dailymart.repository.SupplierRepository
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class SupplierViewModel(context: Context, val repository: SupplierRepository) : ViewModel() {

    val rootSupplier = mutableListOf<Supplier>()
    val listSupplier = MutableLiveData<MutableList<Supplier>>()
    var isLoading = MutableLiveData(false)

    val listShow = MutableLiveData<MutableList<Supplier>>()

    val showSnackbar = MutableLiveData<String>()
    val token = SharedPref.getAccessToken(context)
    val user = SharedPref.getUser(context)

    var typeSupplier = ACTIVE
    private var page = 1
    private var totalPage = 1

    init {
        viewModelScope.launch { getSupplierPage() }
    }

    private suspend fun getSupplierPage() {
        isLoading.postValue(true)
        val result = repository.getSuppliersPage(token, page)
        if (result.isSuccess()) {
            totalPage = result.totalPage
            page++
            rootSupplier.addAll(result.result)
            listSupplier.postValue(rootSupplier)
            delay(100)
            loadShowList()
        } else {
            showSnackbar.postValue("Lấy danh sách thất bại")
        }
        isLoading.postValue(false)
    }

    suspend fun loadMorePage() {
        Log.e(TAG, "loadMorePage2: $page --- $totalPage" )
        if (page >= totalPage) {
            Log.e(TAG, "loadMorePage: $page --- $totalPage" )
            return
        }
        val result = repository.getSuppliersPage(token, page)
        if (result.isSuccess()) {
            rootSupplier.addAll(result.result)
            rootSupplier.toMutableSet()
            listSupplier.postValue(rootSupplier)
            loadShowList()
            page++
        }
    }

    fun loadShowList() {
        val filter = listSupplier.value?.filter { it.status == typeSupplier }?.toMutableList()
            ?: mutableListOf()
        listShow.postValue(filter)
        viewModelScope.launch {
            if (filter.size <= 10) loadMorePage()
        }
    }

    private suspend fun reloadSupplier() {
        page = 1
        rootSupplier.clear()
        getSupplierPage()
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

    fun addNewSupplier(supplier: SupplierParamAdd) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = repository.insertSupplier(supplier, token)
            if (result.isSuccess()) {
                reloadSupplier()
                showSnackbar.postValue(MESSAGE_ADD_SUCCESS)
            } else {
                showSnackbar.postValue(MESSAGE_ADD_FAILED)
            }
            isLoading.postValue(false)

        }
    }

    fun editNewSupplier(id: String, supplier: SupplierParam) {
        viewModelScope.launch {
            isLoading.postValue(true)
            Log.e(TAG, "editNewSupplier: $id --- ${Gson().toJson(supplier)}")
            val result = repository.editSupplier(id, supplier, token)
            if (result.isSuccess()) {
                reloadSupplier()
                showSnackbar.postValue(MESSAGE_EDIT_SUCCESS)
            } else {
                showSnackbar.postValue(MESSAGE_EDIT_FAILED)
            }
            isLoading.postValue(false)
        }
    }

    fun removeSupplier(supplier: Supplier) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val res = repository.removeSupplier(supplier, token)
            if (res.isSuccess()) {
                reloadSupplier()
                showSnackbar.postValue(res.message)
            } else {
                showSnackbar.postValue(MESSAGE_REMOVE_FAILED)
            }
            isLoading.postValue(false)
        }
    }

    fun showDialogAdd(context: Context) {
        AddEditSupplierDialog(context, null, this).show()
    }

    fun clickItems(context: Context, supplier: Supplier) {
        if (user.role == ROLE.staff) {
            showSnackbar.value = "Nhân viên không được sử dụng chức năng này"
            return
        }
        MoreSupplierPopup(context, supplier, this).show()
    }

    fun openRemoveSupplier(context: Context, supplier: Supplier) {
        ConfirmRemoveDialog(context, supplier, this).show()
    }

    fun openEditSupplier(context: Context, supplier: Supplier) {
        AddEditSupplierDialog(context, supplier, this).show()
    }

    fun restoreCategory(categoryParam: CategoryParam) {

    }

    companion object {
        const val TAG = "BXT"
        const val MESSAGE_ADD_SUCCESS = "Thêm thành công"
        const val MESSAGE_ADD_FAILED = "Thêm thất bại"
        const val MESSAGE_REMOVE_SUCCESS = "Vô hiệu hóa thành công"
        const val MESSAGE_REMOVE_FAILED = "Vô hiệu hóa thất bại"
        const val MESSAGE_EDIT_SUCCESS = "Cập nhật thành công"
        const val MESSAGE_EDIT_FAILED = "Cập nhật thất bại"
        const val ACTIVE = true
        const val DISABLE = false
    }
}

