package net.fpoly.dailymart.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.database.ProductInfo
import net.fpoly.dailymart.data.model.Product

interface ProductRepository {
    suspend fun insertProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun deleteAllProduct()
    fun getProductsWithLatestPrice(): Flow<List<ProductInfo>>
    suspend fun getProductById(id: String): ProductInfo
}