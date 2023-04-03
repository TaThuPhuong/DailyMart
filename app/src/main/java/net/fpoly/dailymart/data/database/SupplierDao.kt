package net.fpoly.dailymart.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.Supplier

@Dao
interface SupplierDao {
    @Query("select * from supplier where id =:id")
    suspend fun getSupplierId(id: Int): Supplier?
    /** get AllCategory */
    @Query("select * from supplier")
    fun getAllSupplier(): Flow<List<Supplier>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplier(supplier: Supplier)
    @Update
    suspend fun updateSupplier(supplier: Supplier)
    @Delete
    suspend fun deleteSupplier(supplier: Supplier)
}