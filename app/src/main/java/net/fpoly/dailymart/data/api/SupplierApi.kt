package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.ResultData
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.data.model.SupplierParam
import net.fpoly.dailymart.data.model.SupplierParamAdd
import retrofit2.http.*

interface SupplierApi {

    @POST("supplier")
    suspend fun insertSupplier(@Header("Authorization") token: String, @Body supplier: SupplierParamAdd): ResultData<Supplier>

    @GET("supplier/getAll")
    suspend fun getSuppliers(@Header("Authorization") token: String) : ResultData<ArrayList<Supplier>>

    @DELETE("supplier/{id}")
    suspend fun removeSuppliers(@Header("Authorization") token: String, @Path("id") idSupplier: String) : ResultData<Unit>

    @PUT("supplier/{id}")
    suspend fun updateSuppliers(@Header("Authorization") token: String, @Path("id") idSupplier: String, @Body param: SupplierParam) : ResultData<Unit>

    @GET("supplier")
    suspend fun getSuppliersPage(@Header("Authorization") token: String, @Query("page") page: Int) : ResultData<ArrayList<Supplier>>
}