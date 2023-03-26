package net.fpoly.dailymart.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.model.Product


@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("Delete from products")
    suspend fun deleteAllProduct()

    @Query(
        "SELECT p.id, p.barcode,p.category_id,p.img_product,p.supplier_id,p.unit, pp.sell_price,pp.import_price,pp.effective_date\n" +
                "FROM products p \n" +
                "INNER JOIN (\n" +
                "   SELECT product_id, MAX(effective_date) as max_time\n" +
                "   FROM product_price\n" +
                "   GROUP BY product_id\n" +
                ") AS latest_price\n" +
                "ON p.id = latest_price.product_id\n" +
                "INNER JOIN product_price pp\n" +
                "ON latest_price.product_id = pp.product_id AND latest_price.max_time = pp.effective_date"
    )

    fun getProductsWithLatestPrice(): Flow<List<ProductInfo>>

    @Query(
        "SELECT p.id, p.barcode,p.category_id,p.img_product,p.supplier_id,p.unit, pp.sell_price,pp.import_price,pp.effective_date\n\n" +
                "FROM products p\n" +
                "INNER JOIN (\n" +
                "    SELECT product_id, MAX(effective_date) AS max_date\n" +
                "    FROM product_price\n" +
                "    GROUP BY product_id\n" +
                ") latest_price\n" +
                "ON p.id = latest_price.product_id\n" +
                "INNER JOIN product_price pp\n" +
                "ON latest_price.product_id = pp.product_id AND latest_price.max_date = pp.effective_date\n" +
                "WHERE p.id = :id"
    )

    suspend fun getProductById(id: String): ProductInfo
}

data class ProductInfo(
    var id: String = "",
    var barcode: String = "",
    var category_id: Int = 0,
    var img_product: String = "",
    var supplier_id: String = "",
    var unit: String = "",
    var sell_price: Double = 0.0,
    var import_price: Double = 0.0,
    var effective_date: Long = 0,
)