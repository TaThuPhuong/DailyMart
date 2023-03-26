package net.fpoly.dailymart.view.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.database.ProductInfo
import net.fpoly.dailymart.repository.ProductRepository

class ProductsViewModel(private val pRepository: ProductRepository) : ViewModel() {

    private val _listProduct = MutableLiveData<List<ProductInfo>>(ArrayList())
    val listProduct: LiveData<List<ProductInfo>> = _listProduct

    fun getListProduct() {
        viewModelScope.launch {
            pRepository.getProductsWithLatestPrice().collect { products ->
                _listProduct.value = products.sortedBy { it.name }
            }
        }
    }
}