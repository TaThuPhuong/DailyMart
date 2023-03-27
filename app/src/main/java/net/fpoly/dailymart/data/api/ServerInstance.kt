package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.utils.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ServerInstance {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constant.BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val apiUser by lazy {
            retrofit.create(UserApi::class.java)
        }
        val apiCategory by lazy {
            retrofit.create(CategoryApi::class.java)
        }
        val apiSupplierApi by lazy {
            retrofit.create(SupplierApi::class.java)
        }
        val apiProductApi by lazy {
            retrofit.create(ProductApi::class.java)
        }
        val apiInvoiceApi by lazy {
            retrofit.create(InvoiceApi::class.java)
        }
    }
}