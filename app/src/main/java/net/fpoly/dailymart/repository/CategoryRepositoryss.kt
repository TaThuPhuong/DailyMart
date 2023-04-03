package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.param.CategoryParam
import net.fpoly.dailymart.data.model.param.CategoryParamList
import net.fpoly.dailymart.data.model.param.SupplierParamList
import retrofit2.Call

class CategoryRepositoryss {
    private val supplierService = ServerInstance.apiCategory
    fun insertCategory(token : String , category : CategoryParam) : Call<CategoryParamList>{
        return supplierService.insertCategory(token,category)
    }

    fun getAllCategory(token: String): Call<CategoryParamList> {
        return supplierService.getAllCategory(token)
    }
}