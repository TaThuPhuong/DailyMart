package net.fpoly.dailymart.repository

import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.utils.ROLE

interface UserRepository {
    suspend fun insertUser(user: User)

    fun getAllUser(): Flow<List<User>>?

    suspend fun getUserById(id: String): User?

    suspend fun getUserByRole(role: ROLE): Flow<List<User>>?

    suspend fun getUserDisable(disable: Boolean): Flow<List<User>>?
}