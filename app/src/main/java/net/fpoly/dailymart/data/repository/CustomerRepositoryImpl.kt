package net.fpoly.dailymart.data.repository

import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.Customer
import net.fpoly.dailymart.data.model.CustomerParam
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.repository.CustomerRepository

class CustomerRepositoryImpl : CustomerRepository {

    private val remoteData = ServerInstance.apiCustomer

    override suspend fun getCustomers(token: String): Response<ArrayList<Customer>> {
        return try {
            val res = remoteData.getCustomers(token)
            if (res.isSuccess()) {
                Response.Success(res.result)
            } else {
                Response.Error(res.message)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Response.Error(ex.message.toString())
        }
    }

    override suspend fun addCustomer(token: String, customer: CustomerParam): Response<Unit> {
        return try {
            val res = remoteData.addCustomer(token, customer)
            if (res.isSuccess()) {
                Response.Success(res.result)
            } else {
                Response.Error(res.message)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Response.Error(ex.message.toString())
        }
    }
}