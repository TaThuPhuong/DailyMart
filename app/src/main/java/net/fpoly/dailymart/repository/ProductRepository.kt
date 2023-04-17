package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.data.model.response.ResponseResult

interface ProductRepository {
    suspend fun insertProduct(token: String, productParam: ProductParam): Response<Unit>

    suspend fun getAllProduct(token: String): ResponseResult<List<Product>>

    suspend fun getProductById(token: String, id: String): ResponseResult<Product>

    suspend fun updateProduct(
        token: String,
        id: String,
        productParam: ProductParamUpdate,
    ): Response<Product>

    suspend fun deleteProduct(token: String, id: String): ResponseResult<Unit>

}