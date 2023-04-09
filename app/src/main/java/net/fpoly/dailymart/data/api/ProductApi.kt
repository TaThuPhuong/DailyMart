package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.ProductParam
import net.fpoly.dailymart.data.model.ProductResponse
import net.fpoly.dailymart.data.model.response.ResponseResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @POST("product")
    fun insertProduct(
        @Header("Authorization") token: String,
        @Body productParam: ProductParam,
    ): Call<ResponseBody>

    @GET("product")
    suspend fun getAllProduct(@Header("Authorization") token: String): ResponseResult<List<Product>>

    @GET("product/{id}")
    fun getProduct(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Call<ProductResponse>

    @GET("product/{id}")
    suspend fun getProductById(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): ResponseResult<Product>

    @PUT("product/{id}")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body productParam: ProductParam,
    ): ResponseResult<Product>

    @DELETE("product/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): ResponseResult<Unit>

}
