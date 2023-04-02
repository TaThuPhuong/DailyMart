package net.fpoly.dailymart.data.repository

import net.fpoly.dailymart.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.database.UserDao
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.utils.ROLE

//class UserRepositoryImpl(private val dao: UserDao) : UserRepository {
//    override suspend fun insertUser(user: User) {
//        dao.insertUser(user)
//    }
//
//    override fun getAllUser(): Flow<List<User>>? {
//        return dao.getAllUser()
//    }
//
//    override suspend fun getUserById(id: String): User? {
//        return dao.getUserById(id)
//    }
//
//    override suspend fun getUserByRole(role: ROLE): Flow<List<User>>? {
//        return dao.getUserByRole(role)
//    }
//
//    override suspend fun getUserDisable(disable: Boolean): Flow<List<User>>? {
//        return dao.getUserDisable(disable)
//    }
//}