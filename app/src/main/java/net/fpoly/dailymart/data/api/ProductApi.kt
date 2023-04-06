package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.ListProductResponse
import net.fpoly.dailymart.data.model.ProductResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("product")
    fun getAllProduct(
        @Header("Authorization") token: String
    ): Call<ListProductResponse>

    @GET("product/detailProduct/{id}")
    fun getProduct(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Call<ProductResponse>
}
