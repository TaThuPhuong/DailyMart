package net.fpoly.dailymart.repository

import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.model.Category

interface CategoryRepository {
    suspend fun insertCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun getCategoryId(id: String): Category?

    fun getAllCategory(): Flow<List<Category>>

//    fun searchCategory(name: String): Flow<List<Category>>
}
