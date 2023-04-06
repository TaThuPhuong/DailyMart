package net.fpoly.dailymart.view.add_product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.ProductParam

class AddProductViewModel() : ViewModel() {

    private val TAG = "YingMing"

    private val _product = MutableLiveData(ProductParam())

    private val remoteProduct  = ServerInstance.apiProductApi

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.IdCategoryChange -> {
                _product.value = _product.value?.copy(
                    category = event.id
                )
            }
            is ProductEvent.IdSupplierChange -> {
                _product.value = _product.value?.copy(
                    supplier = event.id
                )
            }
            is ProductEvent.IdChange -> {
                _product.value = _product.value?.copy(
                    barcode = event.id
                )
            }
            is ProductEvent.ImportPriceChange -> {

            }
            is ProductEvent.NameChange -> {
                _product.value = _product.value?.copy(
                    name = event.name
                )
            }
            is ProductEvent.SellPriceChange -> {

            }
            is ProductEvent.UnitChange -> {
                _product.value = _product.value?.copy(
                    unit = event.unit
                )
            }
            is ProductEvent.AddProduct -> {
                Log.d(TAG, "onEvent: ${event.linkImage}")
                _product.value = _product.value?.copy(
                    imageProduct = event.linkImage
                )
                viewModelScope.launch {

                }
            }
        }
    }

}

sealed class ProductEvent {
    data class IdChange(val id: String) : ProductEvent()
    data class NameChange(val name: String) : ProductEvent()
    data class IdSupplierChange(val id: String) : ProductEvent()
    data class IdCategoryChange(val id: String) : ProductEvent()
    data class ImportPriceChange(val price: String) : ProductEvent()
    data class SellPriceChange(val price: String) : ProductEvent()
    data class UnitChange(val unit: String) : ProductEvent()
    data class AddProduct(val linkImage: String) : ProductEvent()
}