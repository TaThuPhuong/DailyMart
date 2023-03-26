package net.fpoly.dailymart.data.repository

import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.database.ProductDao
import net.fpoly.dailymart.data.database.ProductInfo
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.repository.ProductRepository

class ProductRepositoryImpl(private val dao: ProductDao) : ProductRepository {
    override suspend fun insertProduct(product: Product) {
        dao.insertProduct(product)
    }

    override suspend fun deleteProduct(product: Product) {
        dao.deleteProduct(product)
    }

    override suspend fun deleteAllProduct() {
        dao.deleteAllProduct()
    }

    override fun getProductsWithLatestPrice(): Flow<List<ProductInfo>> {
        return dao.getProductsWithLatestPrice()
    }

    override suspend fun getProductById(id: String): ProductInfo {
        return dao.getProductById(id)
    }

}