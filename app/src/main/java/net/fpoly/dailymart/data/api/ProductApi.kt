package net.fpoly.dailymart.data.api

import retrofit2.http.GET

interface ProductApi {


    @GET("api/product")
    fun getAllProduct()
}