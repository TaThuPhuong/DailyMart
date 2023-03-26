package net.fpoly.dailymart.data.repository

import net.fpoly.dailymart.data.database.ProductPriceDao
import net.fpoly.dailymart.data.model.ProductPrice
import net.fpoly.dailymart.repository.ProductPriceRepository

class ProductPriceRepositoryImpl(private val dao: ProductPriceDao) : ProductPriceRepository {
    override suspend fun insertProductPrice(productPrice: ProductPrice) {
        dao.insertProductPrice(productPrice)
    }
}