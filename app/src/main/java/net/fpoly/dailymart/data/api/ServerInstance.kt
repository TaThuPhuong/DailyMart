package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.utils.Constant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServerInstance {
    companion object {
        private val client = OkHttpClient().newBuilder().readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build()
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constant.BASE_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val apiUser: UserApi by lazy {
            retrofit.create(UserApi::class.java)
        }
        val apiCategory: CategoryApi by lazy {
            retrofit.create(CategoryApi::class.java)
        }
        val apiSupplierApi: SupplierApi by lazy {
            retrofit.create(SupplierApi::class.java)
        }
        val apiProduct: ProductApi by lazy {
            retrofit.create(ProductApi::class.java)
        }
        val apiInvoiceApi: InvoiceApi by lazy {
            retrofit.create(InvoiceApi::class.java)
        }
        val apiTask: TaskApi by lazy {
            retrofit.create(TaskApi::class.java)
        }
        val apiReport: ReportApi by lazy {
            retrofit.create(ReportApi::class.java)
        }

        val apiCustomer: CustomerApi by lazy {
            retrofit.create(CustomerApi::class.java)
        }
    }
}