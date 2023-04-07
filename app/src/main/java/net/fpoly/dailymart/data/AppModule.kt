package net.fpoly.dailymart.data

import net.fpoly.dailymart.data.repository.CategoryRepositoryImpl
import net.fpoly.dailymart.data.repository.ProductRepositoryImpl
import net.fpoly.dailymart.data.repository.TaskRepositoryImpl
import net.fpoly.dailymart.data.repository.UserRepositoryImpl
import net.fpoly.dailymart.repository.CategoryRepository
import net.fpoly.dailymart.repository.ProductRepository
import net.fpoly.dailymart.repository.TaskRepository
import net.fpoly.dailymart.repository.UserRepository

object AppModule {
    @Volatile
    var taskRepository: TaskRepository? = null

    @Volatile
    var userRepository: UserRepository? = null

    @Volatile
    var productRepository: ProductRepository? = null

    @Volatile
    var categoryRepository: CategoryRepository? = null


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
}