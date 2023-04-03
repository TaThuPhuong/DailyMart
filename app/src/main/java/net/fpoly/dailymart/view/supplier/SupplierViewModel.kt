package net.fpoly.dailymart.view.supplier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Supplier

class SupplierViewModel() : ViewModel() {
    private val _listSupplier = MutableLiveData<List<Supplier>>(ArrayList())
    val listSupplier: LiveData<List<Supplier>> = _listSupplier

    fun getAllSuppliers(){

    }

    fun insertSuppliers(supplier: Supplier) {

    }
    fun deleteSuppliers(supplier: Supplier) {

    }
}