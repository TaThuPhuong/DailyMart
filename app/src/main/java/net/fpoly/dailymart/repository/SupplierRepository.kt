package net.fpoly.dailymart.repository


import net.fpoly.dailymart.data.model.ResultData
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.data.model.SupplierParam

interface SupplierRepository {
    suspend fun getSuppliers(token: String): ResultData<ArrayList<Supplier>>
    suspend fun insertSupplier(supplier: SupplierParam, token: String): ResultData<Supplier>
    suspend fun editSupplier(id: String, supplier: SupplierParam, token: String): ResultData<Unit>
    suspend fun removeSupplier(supplier: Supplier, token: String): ResultData<Unit>
    suspend fun getSuppliersPage(token: String, page: Int): ResultData<ArrayList<Supplier>>
}
