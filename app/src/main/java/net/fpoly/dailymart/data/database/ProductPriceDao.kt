package net.fpoly.dailymart.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import net.fpoly.dailymart.data.model.ProductPrice

@Dao
interface ProductPriceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductPrice(productPrice: ProductPrice)
}