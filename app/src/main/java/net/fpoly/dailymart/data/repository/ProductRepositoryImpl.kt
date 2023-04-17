package net.fpoly.dailymart.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.api.ProductApi
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.data.model.response.ResponseResult
import net.fpoly.dailymart.repository.ProductRepository

class ProductRepositoryImpl : ProductRepository {

    private val TAG = "YingMing"
    override suspend fun insertProduct(
        token: String,
        productParam: ProductParam,
    ): Response<Unit> = withContext(Dispatchers.IO) {
        val api: ProductApi = ServerInstance.apiProduct
        try {
            val res = api.insertProduct(token, productParam)
            Log.e(TAG, "insertProduct: $res", )
            if (!res.isSuccess()) return@withContext Response.Error(res.message)
            Response.Success(res.result, res.message)
        } catch (e: Exception) {
            Log.e(TAG, "insertProduct Exception: $e")
            Response.Error(ERROR_CONNECT)
        }
    }

    override suspend fun getAllProduct(token: String): ResponseResult<List<Product>> =
        withContext(Dispatchers.IO) {
            val api: ProductApi = ServerInstance.apiProduct
            try {
                api.getAllProduct(token)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: $e")
                ResponseResult(0, ERROR_CONNECT, null)
            }
        }

    override suspend fun getProductById(token: String, id: String): ResponseResult<Product> =
        withContext(Dispatchers.IO) {
            val api: ProductApi = ServerInstance.apiProduct
            try {
                api.getProductById(token, id)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: $e")
                ResponseResult(0, ERROR_CONNECT, null)
            }
        }

    override suspend fun updateProduct(
        token: String,
        id: String,
        productParam: ProductParamUpdate,
    ): Response<Product> = withContext(Dispatchers.IO)
    {
        val api: ProductApi = ServerInstance.apiProduct
        try {
            val res = api.updateProduct(token, id, productParam)
            if (!res.isSuccess()) return@withContext Response.Error(res.message)
            Response.Success(res.result, res.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: $e")
            Response.Error(ERROR_CONNECT)
        }
    }

    override suspend fun deleteProduct(token: String, id: String): ResponseResult<Unit> =
        withContext(Dispatchers.IO) {
            val api: ProductApi = ServerInstance.apiProduct
            try {
                api.deleteProduct(token, id)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: $e")
                ResponseResult(0, ERROR_CONNECT, null)
            }
        }
    companion object {
        const val ERROR_CONNECT = "Máy chủ không phản hồi"
    }
}
