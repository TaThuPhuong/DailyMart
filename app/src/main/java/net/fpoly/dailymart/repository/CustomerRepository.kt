package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.Customer
import net.fpoly.dailymart.data.model.CustomerParam
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.model.response.ResponseResult

interface CustomerRepository {
    suspend fun getCustomers(token: String): Response<ArrayList<Customer>>
    suspend fun addCustomer(token: String, customer: CustomerParam): Response<Unit>
}