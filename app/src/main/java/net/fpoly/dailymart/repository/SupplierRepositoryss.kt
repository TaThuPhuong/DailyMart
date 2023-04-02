package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.param.SupplierParamList
import retrofit2.Call

class SupplierRepositoryss {
     private val supplierService = ServerInstance.apiSupplierApi
  fun getAllSupplier(token: String): Call<SupplierParamList> {
    return supplierService.getAllSupplier(token)

   }
}