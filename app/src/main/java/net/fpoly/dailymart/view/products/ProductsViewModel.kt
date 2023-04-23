package net.fpoly.dailymart.view.products

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.ProductParamUpdate
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.repository.ProductRepository
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class ProductsViewModel(val app: Application, private val repo: ProductRepository) : ViewModel() {

    private val TAG = "YingMing"
    val listProductActive = MutableLiveData<List<Product>>(ArrayList())
    val listProductDisable = MutableLiveData<List<Product>>(ArrayList())
    val message = MutableLiveData("")
    private val _role = MutableLiveData(false)
    private var mProductRecent: Product? = null
    val role: LiveData<Boolean> = _role
    val mUser = SharedPref.getUser(app)
    val getListSuccess = MutableLiveData(false)

    private val mToken = SharedPref.getAccessToken(app)

    init {
        _role.value = mUser.role != ROLE.staff
    }

    fun getListProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getAllProduct(mToken)
            if (res.isSuccess()) {
                res.data?.let { products ->
                    listProductActive.postValue(products.filter { it.status }
                        .sortedByDescending { p -> p.createdAt })
                    listProductDisable.postValue(products.filter { !it.status })
                }
                getListSuccess.postValue(true)
            } else {
                message.postValue(res.message!!)
                getListSuccess.postValue(true)
            }
        }
    }

    fun onRestore(product: Product) {
        val productParamUpdate = ProductParamUpdate(product)
        productParamUpdate.status = true
        viewModelScope.launch(Dispatchers.IO) {
            when (repo.updateProduct(mToken, product.id, productParamUpdate)) {
                is Response.Error -> message.postValue("Phục hồi lỗi")
                is Response.Success -> {
                    getListProducts()
                    message.postValue("Phục hồi thành công")
                }
            }
        }
    }

    fun onDelete(product: Product, onDeleteSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.deleteProduct(mToken, product.id)
            if (res.isSuccess()) {
                mProductRecent = product
                getListProducts()
                onDeleteSuccess()
                message.postValue("Đã chuyển tới thùng rác !!!")
            } else {
                message.postValue("Xóa thất bại")
            }
        }
    }
}