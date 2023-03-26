package net.fpoly.dailymart.data

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import net.fpoly.dailymart.data.database.DailyMartDataBase
import net.fpoly.dailymart.data.repository.ProductPriceRepositoryImpl
import net.fpoly.dailymart.data.repository.ProductRepositoryImpl
import net.fpoly.dailymart.data.repository.TaskRepositoryImpl
import net.fpoly.dailymart.data.repository.UserRepositoryImpl
import net.fpoly.dailymart.repository.ProductPriceRepository
import net.fpoly.dailymart.repository.ProductRepository
import net.fpoly.dailymart.repository.TaskRepository
import net.fpoly.dailymart.repository.UserRepository

object ServiceLocator {
    private var database: DailyMartDataBase? = null

    @Volatile
    var userRepository: UserRepository? = null
        @VisibleForTesting set

    @Volatile
    var taskRepository: TaskRepository? = null
        @VisibleForTesting set

    @Volatile
    var productPriceRepository: ProductPriceRepository? = null
        @VisibleForTesting set

    @Volatile
    var productRepository: ProductRepository? = null
        @VisibleForTesting set

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

    @VisibleForTesting
    private fun createDatabase(context: Context): DailyMartDataBase {
        return Room.databaseBuilder(
            context,
            DailyMartDataBase::class.java,
            DailyMartDataBase.DATABASE_NAME
        ).build()
    }
}