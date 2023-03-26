package net.fpoly.dailymart.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.model.Category

interface CategoryDao {
    /** get id */
    @Query("select * from category where id =:id")
    suspend fun getCategoryId(id: String): Category?
    /** get AllCategory */
    @Query("select * from category")
    fun getAllCategory(): Flow<List<Category>>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)
    @Update
    suspend fun updateCategory(category: Category)
    @Delete
    suspend fun deleteCategory(category: Category)
}