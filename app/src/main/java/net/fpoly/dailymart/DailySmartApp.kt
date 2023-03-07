package net.fpoly.dailymart

import android.app.Application
import android.content.Context
import net.fpoly.dailymart.data.ServiceLocator
import net.fpoly.dailymart.repository.TaskRepository
import net.fpoly.dailymart.repository.UserRepository

class DailySmartApp : Application() {

    val context: Context
        get() = applicationContext

    val userRepository: UserRepository
        get() = ServiceLocator.providerUserRepository(this)

    val taskRepository: TaskRepository
        get() = ServiceLocator.providerTaskRepository(this)

}