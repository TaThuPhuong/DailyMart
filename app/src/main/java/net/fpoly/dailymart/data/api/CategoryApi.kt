package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.CategoryAddParam
import net.fpoly.dailymart.data.model.CategoryParam
import net.fpoly.dailymart.data.model.ResultData
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryApi {

    @GET("industry/getAll")
    suspend fun getAllCategory(@Header("Authorization") token: String): ResultData<List<Category>>

    @POST("industry")
    suspend fun addCategory(
        @Body category: CategoryAddParam,
        @Header("Authorization") token: String
    ): ResultData<Category>

    @PUT("industry/{id}")
    suspend fun updateCategory(
        @Path("id") id: String,
        @Body category: CategoryParam,
        @Header("Authorization") token: String
    ): ResultData<Unit>

    @DELETE("industry/{id}")
    suspend fun removeCategory(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): ResultData<Unit>

    @GET("industry")
    suspend fun getCategoriesPage(@Header("Authorization") token: String, @Query("page") page: Int): ResultData<List<Category>>
}