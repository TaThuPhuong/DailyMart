package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.param.CategoryParam
import net.fpoly.dailymart.data.model.param.CategoryParamList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CategoryApi {

    // thêm mới
    @POST("api/industry")
    fun insertCategory(
        @Header("Authorization") token: String,
        @Body model: CategoryParam,
    ): Call<CategoryParam>

    // update
    @PUT("api/industry/")
    fun updateCategory(
        @Header("Authorization") token: String,
        @Query("id") id: String,
        @Body model: Category,
    ): Call<ResponseBody>

    // get all
    @GET("api/industry")
    fun getAllCategory(@Header("Authorization") token: String): Call<CategoryParamList>
}