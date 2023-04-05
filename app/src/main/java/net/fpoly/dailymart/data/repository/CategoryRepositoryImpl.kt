package net.fpoly.dailymart.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.Category
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
                Response.Success(result.result)
            } catch (ex: Exception) {
                Response.Error(ex.message.toString())
            }
        }

    override suspend fun addCategory(category: CategoryParam, token: String) =
        withContext(Dispatchers.IO) {
            try {
                val result = remoteData.addCategory(category, token)
                if (!result.isSuccess()) return@withContext Response.Error(result.message)
                Response.Success(result.result)
            } catch (ex: Exception) {
                Response.Error(ex.message.toString())
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
            Response.Success(result.result)
        } catch (ex: Exception) {
            Response.Error(ex.message.toString())
        }
    }

    override suspend fun removeCategory(idCategory: String, token: String): Response<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val result = remoteData.removeCategory(idCategory, token)
                if (!result.isSuccess()) return@withContext Response.Error(result.message)
                Response.Success(result.result)
            } catch (ex: Exception) {
                Response.Error(ex.message.toString())
            }
        }
}