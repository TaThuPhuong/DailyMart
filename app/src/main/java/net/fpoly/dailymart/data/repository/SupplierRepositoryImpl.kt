package net.fpoly.dailymart.data.repository

import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.database.SupplierDao
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.repository.SupplierRepository

class SupplierRepositoryImpl(private val dao: SupplierDao)  : SupplierRepository{
    override suspend fun getSupplierId(id: Int): Supplier? {
        return dao.getSupplierId(id)
    }

    override fun getAllSupplier(): Flow<List<Supplier>> {
        return dao.getAllSupplier()
    }

    override suspend fun insertSupplier(supplier: Supplier) {
        return dao.insertSupplier(supplier)
    }

    override suspend fun updateSupplier(supplier: Supplier) {
        return dao.updateSupplier(supplier)
    }

    override suspend fun deleteSupplier(supplier: Supplier) {
        return dao.deleteSupplier(supplier)
    }

}