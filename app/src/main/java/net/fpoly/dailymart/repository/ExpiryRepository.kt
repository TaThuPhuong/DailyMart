package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.Expiry
import net.fpoly.dailymart.data.model.Invoice

interface ExpiryRepository {

    suspend fun insertExpiry(expiry: Expiry)
}