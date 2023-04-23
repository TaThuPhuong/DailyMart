package net.fpoly.dailymart.data.repository

import androidx.work.ListenableWorker.Result.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.api.SupplierApi
import net.fpoly.dailymart.data.model.ResultData
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.data.model.SupplierParam
import net.fpoly.dailymart.data.model.SupplierParamAdd
import net.fpoly.dailymart.repository.SupplierRepository

class SupplierRepositoryImpl(
    private val remoteData: SupplierApi = ServerInstance.apiSupplierApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SupplierRepository {

    override suspend fun getSuppliers(token: String): ResultData<ArrayList<Supplier>> =
        withContext(ioDispatcher) {
            try {
                remoteData.getSuppliers(token)
            } catch (e: Exception) {
                ResultData(status = 0, result = arrayListOf())
            }
        }

    override suspend fun insertSupplier(supplier: SupplierParamAdd, token: String) =
        withContext(ioDispatcher) {
            try {
                remoteData.insertSupplier(token, supplier)
            } catch (e: Exception) {
                ResultData(result = Supplier())
            }
        }

    override suspend fun editSupplier(
        id: String,
        supplier: SupplierParam,
        token: String
    ): ResultData<Unit> = withContext(Dispatchers.IO) {
        try {
            remoteData.updateSuppliers(token, id, supplier)
        } catch (ex: Exception) {
            ResultData(result = Unit)
        }
    }

    override suspend fun removeSupplier(supplier: Supplier, token: String): ResultData<Unit> =
        withContext(ioDispatcher) {
            try {
                remoteData.removeSuppliers(token, supplier.id)
            } catch (ex: Exception) {
                ResultData(result = Unit)
            }
        }

    override suspend fun getSuppliersPage(
        token: String,
        page: Int
    ) = withContext(ioDispatcher) {
        try {
            remoteData.getSuppliersPage(token, page)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ResultData(status = 0, result = arrayListOf())
        }
    }
}