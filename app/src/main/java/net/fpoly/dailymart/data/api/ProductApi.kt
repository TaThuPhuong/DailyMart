package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.ProductParam
import net.fpoly.dailymart.data.model.ProductResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @POST("product")
    fun insertProduct(
        @Header("Authorization") token: String,
        @Body productParam: ProductParam
    ): Call<ResponseBody>

    @GET("product")
    fun getAllProduct(@Header("Authorization") token: String): Call<ResponseBody>

    @GET("product/")
    fun getProduct(
        @Header("Authorization") token: String,
        @Query("id") id: String,
    ): Call<ProductResponse>

    @GET("product/{id}")
    fun getProductById(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Call<ResponseBody>
}
