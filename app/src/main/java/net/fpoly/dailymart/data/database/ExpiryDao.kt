package net.fpoly.dailymart.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import net.fpoly.dailymart.data.model.Expiry

@Dao
interface ExpiryDao {

    @Query("Select * from EXPIRY")
    suspend fun getExpires(): List<Expiry>

    @Query("Select * from EXPIRy where id = :id")
    suspend fun getExpiresById(id: Int): Expiry

    @Insert
    suspend fun insertExpiry(expiry: Expiry)

    @Delete
    suspend fun deleteExpiry(expiry: Expiry)

    @Update
    suspend fun updateExpiry(expiry: Expiry)

}