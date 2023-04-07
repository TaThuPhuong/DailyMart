package net.fpoly.dailymart.data

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
}