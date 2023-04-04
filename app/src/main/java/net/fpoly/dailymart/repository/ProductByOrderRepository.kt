package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.ListProductResponse
import net.fpoly.dailymart.data.model.ProductResponse
import retrofit2.Call

class ProductByOrderRepository {
    private val productService = ServerInstance.apiProductApi

    suspend fun getProductById(id: String, token: String): Call<ProductResponse> {
        return productService.getProduct(token, id)
    }

    suspend fun getAllProduct(token: String): Call<ListProductResponse> {
        return productService.getAllProduct(token)
    }
}
