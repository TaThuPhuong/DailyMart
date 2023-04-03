package net.fpoly.dailymart.view.supplier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.repository.SupplierRepository

class SupplierViewModel(private val supplierRepository: SupplierRepository) : ViewModel() {
    private val _listSupplier = MutableLiveData<List<Supplier>>(ArrayList())
    val listSupplier: LiveData<List<Supplier>> = _listSupplier

    fun getAllSuppliers(){
        viewModelScope.launch {
            supplierRepository.getAllSupplier().collect{
                _listSupplier.value = it
            }
        }
    }

    fun insertSuppliers(supplier: Supplier) {
        viewModelScope.launch {
            supplierRepository.insertSupplier(supplier)
        }
    }
    fun deleteSuppliers(supplier: Supplier) {
        viewModelScope.launch {
            supplierRepository.deleteSupplier(supplier)
        }
    }
}