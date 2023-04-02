package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.ListOrderResponse
import net.fpoly.dailymart.data.model.param.OrderParam
import net.fpoly.dailymart.data.model.OrderResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface InvoiceApi {

    @POST("api/invoice")
    fun insertInvoice(
        @Header("Authorization") token: String,
        @Body model: OrderParam,
    ): Call<OrderResponse>

    @POST("api/invoiceDetail")

    @PUT("api/invoice/")
    fun updateInvoice(
        @Header("Authorization") token: String,
        @Query("id") id: String,
        model: OrderParam,
    ): Call<ResponseBody>

    @GET("api/invoice/")
    fun getInvoice(
        @Header("Authorization") token: String,
        @Query("id") id: String,
    ): Call<ResponseBody>

    @GET("api/invoice")
    fun getAllInvoice(@Header("Authorization") token: String): Call<ListOrderResponse>

    @DELETE("api/invoice/")
    fun deleteInvoice(
        @Header("Authorization") token: String,
        @Query("id") id: String,
    ): Call<ResponseBody>
}
