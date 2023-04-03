package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.ResultData
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.data.model.SupplierParam
import retrofit2.http.*

interface SupplierApi {

    @POST("supplier")
    suspend fun insertSupplier(@Header("Authorization") token: String, @Body supplier: SupplierParam): ResultData<Supplier>

    @GET("supplier")
    suspend fun getSuppliers(@Header("Authorization") token: String) : ResultData<ArrayList<Supplier>>

//    @PUT("api/supplier/")
//    fun updateSupplier(
//        @Header("") token: String,
//        @Query("id") id: String,
//        @Body model: SupplierParam,
//    ): Call<ResponseBody>

//    @GET("api/supplier")
//    fun getAllSupplier(@Header("Authorization") token: String
//    ): Call<SupplierParamList>
//
//    @DELETE("api/supplier/")
//    fun deleteSupplier(@Header("") token: String, @Query("id") id: String): Call<ResponseBody>
}