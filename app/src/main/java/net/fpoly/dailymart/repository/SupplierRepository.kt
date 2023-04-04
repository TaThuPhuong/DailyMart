package net.fpoly.dailymart.repository


import net.fpoly.dailymart.data.model.ResultData
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.data.model.SupplierParam

interface SupplierRepository {
    suspend fun getSuppliers(token: String): ResultData<ArrayList<Supplier>>
    suspend fun insertSupplier(supplier: SupplierParam, token: String): ResultData<Supplier>

}
