package net.fpoly.dailymart.data.repository

import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.database.CategoryDao
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.repository.CategoryRepository

class CategoryRepositoryImpl(private val dao:CategoryDao) : CategoryRepository {
    override suspend fun insertCategory(category: Category) {
        return dao.insertCategory(category)
    }

    override suspend fun updateCategory(category: Category) {
        return dao.updateCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        return dao.deleteCategory(category)
    }

    override suspend fun getCategoryId(id: String): Category? {
        return dao.getCategoryId(id)
    }

    override fun getAllCategory(): Flow<List<Category>>? {
        return dao.getAllCategory()
    }
}