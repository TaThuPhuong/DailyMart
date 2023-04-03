package net.fpoly.dailymart.view.supplier

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.param.SupplierParamList
import net.fpoly.dailymart.repository.SupplierRepositoryss
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SupplierViewModel() : ViewModel() {
    val TAG = "Sup"
    private val _listSupplier = MutableLiveData<SupplierParamList>()
    val listSupplier: LiveData<SupplierParamList> = _listSupplier

    private val supplierRepository = SupplierRepositoryss()

    fun getAllSuppliers(token: String){
        viewModelScope.launch {
            try {
                supplierRepository.getAllSupplier(token).enqueue(object : Callback<SupplierParamList> {
                    override fun onResponse(
                        call: Call<SupplierParamList>,
                        response: Response<SupplierParamList>
                    ) {
                        _listSupplier.value = response.body()
                        Log.d(TAG, "onResponse: ${response.body()}")
                    }
                    override fun onFailure(call: Call<SupplierParamList>, t: Throwable) {
                    }
                })
            }catch (e: Exception){
                Log.e(TAG, "getAll: error: $e")
            }
            }
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