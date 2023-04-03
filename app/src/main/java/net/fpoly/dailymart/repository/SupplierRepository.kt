package net.fpoly.dailymart.repository


import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.model.Supplier

interface SupplierRepository {

    suspend fun getSupplierId(id: Int): Supplier?
    fun getAllSupplier(): Flow<List<Supplier>>
    suspend fun insertSupplier(supplier: Supplier)
    suspend fun updateSupplier(supplier: Supplier)
    suspend fun deleteSupplier(supplier: Supplier)
}
