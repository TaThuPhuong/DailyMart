package net.fpoly.dailymart.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.CategoryAddParam
import net.fpoly.dailymart.data.model.CategoryParam
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.repository.CategoryRepository

class CategoryRepositoryImpl : CategoryRepository {

    private val remoteData = ServerInstance.apiCategory

    override suspend fun getAllCategory(token: String): Response<List<Category>> =
        withContext(Dispatchers.IO) {
            try {
                val result = remoteData.getAllCategory(token)
                if (!result.isSuccess()) return@withContext Response.Error(result.message)
                Response.Success(result.result, result.message)
            } catch (ex: Exception) {
                Response.Error(ERROR_CONNECTED)
            }
        }

    override suspend fun addCategory(category: CategoryAddParam, token: String) =
        withContext(Dispatchers.IO) {
            try {
                val result = remoteData.addCategory(category, token)
                if (!result.isSuccess()) return@withContext Response.Error(result.message)
                Response.Success(result.result, result.message)
            } catch (ex: Exception) {
                Response.Error(ERROR_CONNECTED)
            }
        }

    override suspend fun updateCategory(
        idCategory: String,
        category: CategoryParam,
        token: String
    ) = withContext(Dispatchers.IO) {
        try {
            val result = remoteData.updateCategory(idCategory, category, token)
            if (!result.isSuccess()) return@withContext Response.Error(result.message)
            Response.Success(result.result, result.message)
        } catch (ex: Exception) {
            Response.Error(ERROR_CONNECTED)
        }
    }

    override suspend fun removeCategory(idCategory: String, token: String): Response<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val result = remoteData.removeCategory(idCategory, token)
                if (!result.isSuccess()) return@withContext Response.Error(result.message)
                Response.Success(result.result, result.message)
            } catch (ex: Exception) {
                Response.Error(ERROR_CONNECTED)
            }
        }

    override suspend fun getCategoriesPage(
        token: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        try {
            val result = remoteData.getCategoriesPage(token, page)
            return@withContext if (result.isSuccess()) Response.Success(
                result.result,
                result.message,
                result.totalPage
            ) else Response.Error(
                result.message
            )

        } catch (ex: Exception) {
            ex.printStackTrace()
            Response.Error(ex.message.toString())
        }
    }

    companion object {
        const val ERROR_CONNECTED = "Kết nối thất bại"
    }
}