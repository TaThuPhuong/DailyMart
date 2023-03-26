package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.ProductPrice

interface ProductPriceRepository {
    suspend fun insertProductPrice(productPrice: ProductPrice)
}