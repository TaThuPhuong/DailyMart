package net.fpoly.dailymart.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.api.SupplierApi
import net.fpoly.dailymart.data.model.ResultData
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.data.model.SupplierParam
import net.fpoly.dailymart.repository.SupplierRepository

class SupplierRepositoryImpl(
    private val remoteData: SupplierApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SupplierRepository {

    override suspend fun getSuppliers(token: String): ResultData<ArrayList<Supplier>> =
        withContext(ioDispatcher) {
            remoteData.getSuppliers(token)
        }

    override suspend fun insertSupplier(supplier: SupplierParam, token: String) = withContext(Dispatchers.IO) {
        remoteData.insertSupplier(token, supplier)
    }
}