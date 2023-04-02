package net.fpoly.dailymart.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.database.ExpiryDao
import net.fpoly.dailymart.data.model.Expiry
import net.fpoly.dailymart.repository.ExpiryRepository

class ExpiryRepositoryImpl(
    private val expiryDao: ExpiryDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExpiryRepository{
    override suspend fun insertExpiry(expiry: Expiry) {
        withContext(ioDispatcher){
            expiryDao.insertExpiry(expiry)
        }
    }
}