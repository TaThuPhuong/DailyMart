package net.fpoly.dailymart.data.repository

import net.fpoly.dailymart.data.database.UserDao
import net.fpoly.dailymart.repository.UserRepository

class UserRepositoryImpl(private val dao: UserDao) : UserRepository {
}