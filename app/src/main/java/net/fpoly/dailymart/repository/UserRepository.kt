package net.fpoly.dailymart.repository

import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.api.UserApi
import net.fpoly.dailymart.data.database.UserDao
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.utils.ROLE

interface UserRepository {
    suspend fun insertUser(user: User)

    fun getAllUser(): Flow<List<User>>?

    suspend fun getUserById(id: String): User?

    fun getUserByRole(role: ROLE): Flow<List<User>>?

    fun getUserDisable(disable: Boolean): Flow<List<User>>?
}