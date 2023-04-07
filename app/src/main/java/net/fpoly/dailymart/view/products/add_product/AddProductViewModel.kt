package net.fpoly.dailymart.view.products.add_product

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.ProductParam
import net.fpoly.dailymart.data.model.checkValidate
import net.fpoly.dailymart.repository.ProductRepository
import net.fpoly.dailymart.utils.SharedPref

class AddProductViewModel(private val app: Application, private val repo: ProductRepository) :
    ViewModel() {

    private val TAG = "YingMing"

    private val mToken = SharedPref.getAccessToken(app)
    private val _product = MutableLiveData(ProductParam())
    val actionSuccess = MutableLiveData(false)
    val message = MutableLiveData<String>(null)
    val checkValidate = MutableLiveData(false)

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.IdCategoryChange -> {
                checkValidate.value = _product.value?.checkValidate()
                _product.value = _product.value?.copy(
                    category = event.id
                )
            }
            is ProductEvent.IdSupplierChange -> {
                checkValidate.value = _product.value?.checkValidate()
                _product.value = _product.value?.copy(
                    supplier = event.id
                )
            }
            is ProductEvent.IdChange -> {
                checkValidate.value = _product.value?.checkValidate()
                _product.value = _product.value?.copy(
                    barcode = event.id
                )
            }
            is ProductEvent.ImportPriceChange -> {
                checkValidate.value = _product.value?.checkValidate()
                try {
                    _product.value = _product.value?.copy(
                        importPrice = event.price.toDouble()
                    )
                } catch (e: Exception) {
                    _product.value = _product.value?.copy(
                        importPrice = 0.0
                    )
                }
            }
            is ProductEvent.NameChange -> {
                checkValidate.value = _product.value?.checkValidate()
                _product.value = _product.value?.copy(
                    name = event.name
                )
            }
            is ProductEvent.SellPriceChange -> {
                checkValidate.value = _product.value?.checkValidate()
                try {
                    _product.value = _product.value?.copy(
                        sellPrice = event.price.toDouble()
                    )
                } catch (e: Exception) {
                    _product.value = _product.value?.copy(
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
                _product.value = _product.value?.copy(
                    imageProduct = event.linkImage
                )
                checkValidate.value?.let {
                    if (it) {
                        viewModelScope.launch {
                            val res = repo.insertProduct(mToken, _product.value!!)
                            if (res.isSuccess()) {
                                message.postValue("Thêm thành công")
                                actionSuccess.postValue(true)
                            } else {
                                message.postValue("Thêm thất bại")
                                actionSuccess.postValue(false)
                            }
                        }
                    } else {
                        message.postValue("Chưa nhập đủ dữ liệu")
                    }
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