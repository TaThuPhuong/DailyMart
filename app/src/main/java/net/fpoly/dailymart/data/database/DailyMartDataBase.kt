package net.fpoly.dailymart.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.*

@Database(
    entities = [User::class, Task::class, Category::class, Expiry::class, Invoice::class,
        InvoiceDetail::class, Product::class, ProductPrice::class, Supplier::class],
    version = 1,
    exportSchema = false
)
abstract class DailyMartDataBase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val taskDao: TaskDao
    abstract val categoryDao: CategoryDao
    abstract val expiryDao: ExpiryDao
    abstract val invoiceDao: InvoiceDao
    abstract val invoiceDetailDao: InvoiceDetailDao
    abstract val productDao: ProductDao
    abstract val productPriceDao: ProductPriceDao
    abstract val recentActivityDao: RecentActivityDao
    abstract val supplierDao: SupplierDao

    companion object {
        const val DATABASE_NAME = "daily_mart_db"
    }
}