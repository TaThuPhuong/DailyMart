package net.fpoly.dailymart.data

import android.content.Context
import android.provider.ContactsContract.Data
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import net.fpoly.dailymart.data.database.Database
import net.fpoly.dailymart.data.repository.*
import net.fpoly.dailymart.repository.*

object AppModule {
    @Volatile
    var taskRepository: TaskRepository? = null

    @Volatile
    var userRepository: UserRepository? = null

    @Volatile
    var productRepository: ProductRepository? = null

    @Volatile
    var categoryRepository: CategoryRepository? = null

    @Volatile
    var supplierRepository: SupplierRepository? = null

    @Volatile
    var notificationRepo: NotificationRepository? = null

    @Volatile
    var reportRepository: ReportRepository? = null

    private var database: Database? = null
    fun providerTaskRepository(): TaskRepository {
        synchronized(this) {
            return taskRepository ?: TaskRepositoryImpl()
        }
    }

    fun providerUserRepository(): UserRepository {
        synchronized(this) {
            return userRepository ?: UserRepositoryImpl()
        }
    }

    fun providerProductRepository(): ProductRepository {
        synchronized(this) {
            return productRepository ?: ProductRepositoryImpl()
        }
    }

    fun providerCategoryRepository(): CategoryRepository {
        synchronized(this) {
            return categoryRepository ?: CategoryRepositoryImpl()
        }
    }

    fun providerSupplierRepository(): SupplierRepository {
        synchronized(this) {
            return supplierRepository ?: SupplierRepositoryImpl()
        }
    }
    private fun createNotificationRepositoryImpl(context: Context): NotificationRepositoryImpl {
        val database = database ?: createDatabase(context)
        return NotificationRepositoryImpl(database.notificationDao)
    }

    fun providerNotificationRepository(context: Context): NotificationRepository {
        synchronized(this) {
            return notificationRepo ?:  createNotificationRepositoryImpl(context)
        }
    }
    fun providerReportRepository(): ReportRepository {
        synchronized(this) {
            return reportRepository ?: ReportRepositoryImpl()
        }
    }

    @VisibleForTesting
    fun createDatabase(context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            Database.DATABASE_NAME
        ).build()
    }
}