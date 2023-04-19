package net.fpoly.dailymart.view.products

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.api.ServerInstance.Companion.apiProduct
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.ProductParam
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.repository.ProductRepository
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

class ProductsViewModel(val app: Application, private val repo: ProductRepository) : ViewModel() {

    private val TAG = "YingMing"
    private val _listProduct = MutableLiveData<List<Product>>(ArrayList())
    val listProduct: LiveData<List<Product>> = _listProduct
    val message = MutableLiveData<String>(null)
    private val _role = MutableLiveData(false)
    private var mProductRecent: Product? = null
    val role: LiveData<Boolean> = _role
    val mUser = SharedPref.getUser(app)
    val getListSuccess = MutableLiveData(false)

    private val mToken = SharedPref.getAccessToken(app)

    init {
        _role.value = mUser!!.role != ROLE.staff
    }

    fun getListProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.getAllProduct(mToken)
            if (res.isSuccess()) {
                _listProduct.postValue(res.data!!)
                message.postValue(res.message!!)
                getListSuccess.postValue(true)
            } else {
                message.postValue(res.message!!)
                getListSuccess.postValue(true)
            }
        }
    }

    fun onRestore() {
        mProductRecent?.let {
            viewModelScope.launch(Dispatchers.IO) {
                when (repo.insertProduct(mToken, ProductParam(it))) {
                    is Response.Error -> {
                        message.postValue("Phục hồi lỗi")
                    }
                    is Response.Success -> {
                        getListProducts()
                        message.postValue("Phục hồi thành công")
                        mProductRecent = null
                    }
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
            } else {
                message.postValue("Xóa thất bại")
            }
        }
    }
}