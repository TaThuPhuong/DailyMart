package net.fpoly.dailymart.view.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.data.model.Product

class ProductsViewModel() : ViewModel() {

    private val _listProduct = MutableLiveData<List<Product>>(ArrayList())
    val listProduct: LiveData<List<Product>> = _listProduct

    fun getListProduct() {

    }
}