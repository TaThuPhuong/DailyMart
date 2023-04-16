package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.data.model.InvoiceRefund
import net.fpoly.dailymart.data.model.ResultData
import net.fpoly.dailymart.data.model.param.InvoiceParam
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface InvoiceApi {

    @GET("invoice/getAll")
    suspend fun getInvoices(@Header("Authorization") token: String): ResultData<ArrayList<Invoice>>

    @GET("invoice/{id}")
    suspend fun getInvoice(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ResultData<Invoice>

    @POST("invoice")
    suspend fun insertInvoice(
        @Header("Authorization") token: String,
        @Body invoice: InvoiceParam
    ): ResultData<Unit>

    @PUT("invoice/{id}")
    suspend fun updateInvoice(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body invoice: InvoiceParam
    ): ResultData<Invoice>

    @DELETE("invoice/{id}")
    suspend fun removeInvoice(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ResultData<Unit>

    @POST("invoice/refund")
    suspend fun refundInvoice(
        @Header("Authorization") token: String,
        @Body invoice: InvoiceRefund
    ): ResultData<Unit>
}
