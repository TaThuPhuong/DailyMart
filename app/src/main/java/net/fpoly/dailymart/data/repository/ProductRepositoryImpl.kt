package net.fpoly.dailymart.data.repository

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.api.ProductApi
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.ProductParam
import net.fpoly.dailymart.data.model.ProductParamUpdate
import net.fpoly.dailymart.data.model.response.ResponseResult
import net.fpoly.dailymart.repository.ProductRepository

class ProductRepositoryImpl
    (
    private val api: ProductApi = ServerInstance.apiProduct,
    private val coroutineScope: CoroutineDispatcher = Dispatchers.IO,
) : ProductRepository {

    private val TAG = "YingMing"

//    override suspend fun insertProduct(
//        token: String,
//        productParam: ProductParam,
//    ): ResponseResult<Unit> = withContext(coroutineScope) {
//        try {
//            api.insertProduct(token, productParam)
//        } catch (e: Exception) {
//            Log.e(TAG, "Exception: $e")
//            ResponseResult(0, "Lỗi", null)
//        }
//    }

    override suspend fun getAllProduct(token: String): ResponseResult<List<Product>> =
        withContext(coroutineScope) {
            try {
                api.getAllProduct(token)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: $e")
                ResponseResult(0, "Máy chủ không phản hồi", null)
            }
        }

    override suspend fun getProductById(token: String, id: String): ResponseResult<Product> =
        withContext(coroutineScope) {
            try {
                api.getProductById(token, id)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: $e")
                ResponseResult(0, "Máy chủ không phản hồi", null)
            }
        }

    override suspend fun updateProduct(
        token: String,
        id: String,
        productParam: ProductParamUpdate,
    ): ResponseResult<Product> = withContext(coroutineScope) {
        try {
            api.updateProduct(token, id, productParam)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: $e")
            ResponseResult(0, "Máy chủ không phản hồi", null)
        }
    }

    override suspend fun deleteProduct(token: String, id: String): ResponseResult<Unit> =
        withContext(coroutineScope) {
            try {
                api.deleteProduct(token, id)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: $e")
                ResponseResult(0, "Máy chủ không phản hồi", null)
            }
        }
}
