package net.fpoly.dailymart.view.add_product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.ProductPrice

class AddProductViewModel() : ViewModel() {

    private val TAG = "YingMing"

    private val _product = MutableLiveData(Product())

    private val _productPrice = MutableLiveData(ProductPrice())

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.IdCategoryChange -> {
                _product.value = _product.value?.copy(
                    categoryId = event.id
                )
            }
            is ProductEvent.IdSupplierChange -> {
                _product.value = _product.value?.copy(
                    supplierId = event.id
                )
            }
            is ProductEvent.IdChange -> {
                _product.value = _product.value?.copy(
                    id = event.id,
                    barcode = event.id
                )
                _productPrice.value = _productPrice.value?.copy(
                    productId = event.id,
                    effectiveDate = System.currentTimeMillis()
                )
            }
            is ProductEvent.ImportPriceChange -> {
                try {
                    _productPrice.value = _productPrice.value?.copy(
                        importPrice = event.price.toDouble()
                    )
                } catch (e: Exception) {
                    _productPrice.value = _productPrice.value?.copy(
                        importPrice = 0.0
                    )
                }
            }
            is ProductEvent.NameChange -> {
                _product.value = _product.value?.copy(
                    name = event.name
                )
            }
            is ProductEvent.SellPriceChange -> {
                try {
                    _productPrice.value = _productPrice.value?.copy(
                        sellPrice = event.price.toDouble()
                    )
                } catch (e: Exception) {
                    _productPrice.value = _productPrice.value?.copy(
                        sellPrice = 0.0
                    )
                }
            }
            is ProductEvent.UnitChange -> {
                _product.value = _product.value?.copy(
                    unit = event.unit
                )
            }
            is ProductEvent.AddProduct -> {
                Log.d(TAG, "onEvent: ${event.linkImage}")
                _product.value = _product.value?.copy(
                    img_product = event.linkImage
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