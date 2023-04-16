package net.fpoly.dailymart.view.products.add_product

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.data.model.response.ResponseResult
import net.fpoly.dailymart.repository.CategoryRepository
import net.fpoly.dailymart.repository.ProductRepository
import net.fpoly.dailymart.repository.SupplierRepository
import net.fpoly.dailymart.utils.SharedPref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

class AddProductViewModel(
    private val app: Application,
    private val productRepo: ProductRepository,
    private val supplierRepo: SupplierRepository,
    private val categoryRepo: CategoryRepository,
) :
    ViewModel() {

    private val TAG = "YingMing"

    private val mToken = SharedPref.getAccessToken(app)
    private val _product = MutableLiveData(ProductParam())
    val product: LiveData<ProductParam> = _product
    val actionSuccess = MutableLiveData(false)
    val message = MutableLiveData<String>()
    val checkValidate = MutableLiveData(false)

    private val _listCategory = MutableLiveData<List<Category>>(ArrayList())
    val listCategory: LiveData<List<Category>> = _listCategory

    private val _listSupplier = MutableLiveData<List<Supplier>>(ArrayList())
    val listSupplier: LiveData<List<Supplier>> = _listSupplier

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val resCategory = categoryRepo.getAllCategory(mToken)
            val resSupplier = supplierRepo.getSuppliers(mToken)

            when (resCategory) {
                is Response.Success -> {
                    _listCategory.postValue(resCategory.data!!)
                }
                is Response.Error -> {

                }
            }
            if (resSupplier.isSuccess()) {
                _listSupplier.postValue(resSupplier.result!!)
            }
        }
    }

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
                        importPrice = event.price.toInt()
                    )
                } catch (e: Exception) {
                    _product.value = _product.value?.copy(
                        importPrice = 0
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
                        sellPrice = event.price.toInt()
                    )
                } catch (e: Exception) {
                    _product.value = _product.value?.copy(
                        sellPrice = 0
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
                        viewModelScope.launch(Dispatchers.IO) {
                            try {
                                ServerInstance.apiProduct.insertProduct(mToken, _product.value!!)
                                    .enqueue(object : Callback<ResponseBody> {
                                        override fun onResponse(
                                            call: Call<ResponseBody>,
                                            response: retrofit2.Response<ResponseBody>,
                                        ) {
                                            if (response.isSuccessful) {
                                                message.postValue("Thêm thành công")
                                                actionSuccess.postValue(true)
                                                Log.e(TAG, "body: ${response.body()?.string()}")
                                                Log.e(
                                                    TAG,
                                                    "errorBody: ${response.errorBody()?.string()}",
                                                )
                                            } else {
                                                message.postValue(response.message())
                                                actionSuccess.postValue(false)
                                                Log.e(TAG, "body: ${response.body()?.string()}")
                                                Log.e(
                                                    TAG,
                                                    "errorBody: ${response.errorBody()?.string()}",
                                                )
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<ResponseBody>,
                                            t: Throwable,
                                        ) {
                                            Log.e(TAG, "onFailure: $t")
                                        }
                                    })
                            } catch (e: Exception) {
                                Log.e(TAG, "Exception: $e")
                            }

                        }
                    } else {
                        message.postValue("Chưa nhập đủ dữ liệu")
                    }
                }
            }
        }
    }

    private fun checkBarcode(barcode: String) {

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