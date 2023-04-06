package net.fpoly.dailymart.view.order

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.ListOrderResponse
import net.fpoly.dailymart.data.model.ListProductResponse
import net.fpoly.dailymart.data.model.OrderResponse
import net.fpoly.dailymart.data.model.ProductResponse
import net.fpoly.dailymart.data.model.param.OrderParam
import net.fpoly.dailymart.repository.OrderRepository
import net.fpoly.dailymart.repository.ProductByOrderRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel() : ViewModel() {
    private val TAG = "OrderViewModel"

    private val invoiceRepository = OrderRepository()
    private val _newOrder = MutableLiveData<OrderResponse>()
    val newOrder: LiveData<OrderResponse> = _newOrder
    private val _listOrder = MutableLiveData<ListOrderResponse>()
    val listOrder: LiveData<ListOrderResponse> = _listOrder

    private val productRepository = ProductByOrderRepository()
    private val _product = MutableLiveData<ProductResponse>()
    val product: LiveData<ProductResponse> = _product
    private lateinit var mLoadingDialog: LoadingDialog

    fun initLoadDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context)
    }

    fun insertOrder(invoice: OrderParam, token: String) {
        viewModelScope.launch {
            mLoadingDialog.showLoading()
            try {
                invoiceRepository.insertInvoice(invoice, token)
                    .enqueue(object : Callback<OrderResponse> {
                        override fun onResponse(
                            call: Call<OrderResponse>,
                            response: Response<OrderResponse>,
                        ) {
                            Log.d(TAG, "onRespons: add success: ${response.body()}")
                            _newOrder.value = response.body()
                            mLoadingDialog.hideLoading()
                        }

                        override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                            Log.e(TAG, "onFailure: add failed: $t")
                            mLoadingDialog.hideLoading()
                        }
                    })
            } catch (e: Exception) {
                Log.e(TAG, "onFailure: failed: $e")
                mLoadingDialog.hideLoading()
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun getOrders(token: String) {
        viewModelScope.launch {
            try {
                invoiceRepository.getInvoices(token).enqueue(object : Callback<ListOrderResponse> {
                    override fun onResponse(
                        call: Call<ListOrderResponse>,
                        response: Response<ListOrderResponse>,
                    ) {
                        Log.d(TAG, "getOrders: data:  ${response.body()}")
                        _listOrder.value = response.body()
                    }

                    override fun onFailure(call: Call<ListOrderResponse>, t: Throwable) {
                        _listOrder.value = null
                        Log.e(TAG, "onFailure: order: $t")
                    }
                })
            } catch (e: Exception) {
                _listOrder.value = null
                e.printStackTrace()
            }
        }
    }

    fun getAllProduct(token: String) {
        viewModelScope.launch {
            mLoadingDialog.showLoading()
            try {
                productRepository.getAllProduct(token)
                    .enqueue(object : Callback<ListProductResponse> {
                        override fun onResponse(
                            call: Call<ListProductResponse>,
                            response: Response<ListProductResponse>,
                        ) {
                            Log.d(TAG, "onResponse: list product: ${response.body()}")
                            mLoadingDialog.hideLoading()
                        }

                        override fun onFailure(call: Call<ListProductResponse>, t: Throwable) {
                            Log.e(TAG, "onFailure: list product: $t")
                            mLoadingDialog.hideLoading()
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                mLoadingDialog.hideLoading()
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun getProduct(id: String, token: String) {
        viewModelScope.launch {
            mLoadingDialog.showLoading()
            try {
                productRepository.getProductById(id, token)
                    .enqueue(object : Callback<ProductResponse> {
                        override fun onResponse(
                            call: Call<ProductResponse>,
                            response: Response<ProductResponse>,
                        ) {
                            Log.d(TAG, "onResponse: product: ${response.body()}")
                            _product.value = response.body()
                            mLoadingDialog.hideLoading()
                        }

                        override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                            _product.value = null
                            Log.e(TAG, "onFailure: failed: $t")
                            mLoadingDialog.hideLoading()
                        }
                    })
            } catch (e: Exception) {
                _product.value = null
                e.printStackTrace()
                mLoadingDialog.hideLoading()
            }
        }
    }

//    private val _product = MutableLiveData<ProductInfo>(ProductInfo())
//    val product: LiveData<ProductInfo> = _product
//    private val _listOrder = MutableLiveData<List<OrderInfo>>(ArrayList())
//    val listOrder: LiveData<List<OrderInfo>> = _listOrder

//    fun insertOrder(invoice: Invoice, invoiceD: InvoiceDetail, expiry: Expiry) = viewModelScope.launch {
//        iRepository.insertInvoice(invoice)
//        idRepository.insertInvoiceD(invoiceD)
//        eRepository.insertExpiry(expiry)
//    }
//
//    fun updateOrder(invoice: Invoice, invoiceD: InvoiceDetail) = viewModelScope.launch {
//        iRepository.updateInvoice(invoice)
//        idRepository.updateInvoiceD(invoiceD)
//    }
//
//    fun deleteOrder(invoice: Invoice, invoiceD: InvoiceDetail) = viewModelScope.launch {
//        iRepository.deleteInvoice(invoice)
//        idRepository.deleteInvoiceD(invoiceD)
//    }
//
//    fun findProductById(productId: String) = viewModelScope.launch {
//        _product.value = pRepository.getProductById(productId)
//    }
//
//    fun detailOrder(context: Context, invoice: Invoice) {
//    }
//
//    fun getAllOrder() {
//        viewModelScope.launch {
//            iRepository.getOrders().collect { orders ->
//                _listOrder.value = orders.sortedBy { it.name }
//            }
//        }
//    }
}
