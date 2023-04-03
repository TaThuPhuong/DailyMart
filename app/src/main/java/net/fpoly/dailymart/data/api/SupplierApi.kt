package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.param.SupplierParam
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface SupplierApi {

    @POST("api/supplier")
    fun insertSupplier(@Header("") token: String, @Body model: SupplierParam): Call<ResponseBody>

    @PUT("api/supplier/")
    fun updateSupplier(
        @Header("") token: String,
        @Query("id") id: String,
        @Body model: SupplierParam,
    ): Call<ResponseBody>

    @GET("api/supplier")
    fun getAllSupplier(@Header("") token: String): Call<ResponseBody>

    @DELETE("api/supplier/")
    fun deleteSupplier(@Header("") token: String, @Query("id") id: String): Call<ResponseBody>
}