package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.CategoryParam
import net.fpoly.dailymart.data.model.Response

interface CategoryRepository {
    suspend fun getAllCategory(token: String): Response<List<Category>>
    suspend fun addCategory(category: CategoryParam, token: String): Response<Category>

    suspend fun updateCategory(
        idCategory: String,
        category: CategoryParam,
        token: String
    ): Response<Unit>

    suspend fun removeCategory(idCategory: String, token: String): Response<Unit>
}