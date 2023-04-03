package net.fpoly.dailymart.data

import net.fpoly.dailymart.repository.UserRepository
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.database.DailyMartDataBase
import net.fpoly.dailymart.data.repository.*
import net.fpoly.dailymart.repository.*

object ServiceLocator {
    private var database: DailyMartDataBase? = null

    @Volatile
    var userRepository: UserRepository? = null
        @VisibleForTesting set

    @Volatile
    var taskRepository: TaskRepository? = null
        @VisibleForTesting set

    @Volatile
    var categoryRepository: CategoryRepository? = null
        @VisibleForTesting set

    @Volatile
    var supplierRepository: SupplierRepository? = null
        @VisibleForTesting set

    @Volatile
    var productPriceRepository: ProductPriceRepository? = null
        @VisibleForTesting set

    @Volatile
    var productRepository: ProductRepository? = null
        @VisibleForTesting set

    @Volatile
    var invoiceRepository: InvoiceRepository? = null

    @Volatile
    var invoiceDetailRepository: InvoiceDetailRepository? = null

    @Volatile
    var expiryRepository: ExpiryRepository? = null

    fun providerExpiryRepository(context: Context): ExpiryRepository {
        synchronized(this) {
            return expiryRepository ?: expiryRepository ?: createExpiryRepositoryImpl(context)
        }
    }

    private fun createExpiryRepositoryImpl(context: Context): ExpiryRepositoryImpl {
        val database = database ?: createDatabase(context)
        return ExpiryRepositoryImpl(database.expiryDao)
    }

    fun providerInvoiceDetailRepository(context: Context): InvoiceDetailRepository {
        synchronized(this) {
            return invoiceDetailRepository ?: invoiceDetailRepository ?: createInvoiceDetailRepositoryImpl(context)
        }
    }

    private fun createInvoiceDetailRepositoryImpl(context: Context): InvoiceDetailRepositoryImpl {
        val database = database ?: createDatabase(context)
        return InvoiceDetailRepositoryImpl(database.invoiceDetailDao)
    }

    fun providerUserRepository(context: Context): UserRepository {
        synchronized(this) {
            return userRepository ?: userRepository ?: createUserRepositoryImpl(context)
        }
    }

    private fun createUserRepositoryImpl(context: Context): UserRepositoryImpl {
        val database = database ?: createDatabase(context)
        return UserRepositoryImpl(database.userDao)
    }

    fun providerTaskRepository(context: Context): TaskRepository {
        synchronized(this) {
            return taskRepository ?: taskRepository ?: createTaskRepositoryImpl(context)
        }
    }

    private fun createTaskRepositoryImpl(context: Context): TaskRepositoryImpl {
        val database = database ?: createDatabase(context)
        return TaskRepositoryImpl(database.taskDao)
    }

    fun providerCategoryRepository(context: Context): CategoryRepository {
        synchronized(this) {
            return categoryRepository ?: categoryRepository ?: createCategoryRepositoryImpl(context)
        }
    }
    private fun createCategoryRepositoryImpl(context: Context): CategoryRepository {
        val database = database ?: createDatabase(context)
        return CategoryRepositoryImpl(database.categoryDao)
    }
    fun providerSupplierRepository(context: Context): SupplierRepository {
        synchronized(this) {
            return supplierRepository ?: supplierRepository ?: createSupplierRepositoryImpl(context)
        }
    }
    private fun createSupplierRepositoryImpl(context: Context): SupplierRepository {
        val database = database ?: createDatabase(context)
        return SupplierRepositoryImpl(ServerInstance.apiSupplierApi)
    }
    fun providerProductPriceRepository(context: Context): ProductPriceRepository {
        synchronized(this) {
            return productPriceRepository ?: productPriceRepository
                ?: createProductPriceRepositoryImpl(context)
        }
    }

    private fun createProductPriceRepositoryImpl(context: Context): ProductPriceRepositoryImpl {
        val database = database ?: createDatabase(context)
        return ProductPriceRepositoryImpl(database.productPriceDao)
    }

    fun providerProductRepository(context: Context): ProductRepository {
        synchronized(this) {
            return productRepository ?: productRepository
                ?: createProductRepositoryImpl(context)
        }
    }

    private fun createProductRepositoryImpl(context: Context): ProductRepositoryImpl {
        val database = database ?: createDatabase(context)
        return ProductRepositoryImpl(database.productDao)
    }
    fun providerInvoiceRepository(context: Context): InvoiceRepository {
        synchronized(this) {
            return invoiceRepository ?: createInvoiceRepository(context)
        }
    }

    private fun createInvoiceRepository(context: Context): InvoiceRepositoryImpl {
        val database = database ?: createDatabase(context)
        return InvoiceRepositoryImpl(database.invoiceDao)
    }

    @VisibleForTesting
    private fun createDatabase(context: Context): DailyMartDataBase {
        return Room.databaseBuilder(
            context,
            DailyMartDataBase::class.java,
            DailyMartDataBase.DATABASE_NAME
        ).build()
    }
}
