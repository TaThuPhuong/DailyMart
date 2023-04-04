package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.ProductResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ProductApi {
    @GET("api/product")
    fun getAllProduct()

    @GET("api/product/")
    fun getProduct(
        @Header("Authorization") token: String,
        @Query("id") id: String,
    ): Call<ProductResponse>
}
