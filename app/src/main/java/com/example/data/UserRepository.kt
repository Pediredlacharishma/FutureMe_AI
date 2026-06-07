package com.example.data

import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    fun getUserFlow(email: String): Flow<UserEntity?> = userDao.getUserFlow(email)

    suspend fun getUser(email: String): UserEntity? = userDao.getUser(email)

    suspend fun getAllUsers(): List<UserEntity> = userDao.getAllUsers()

    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)

    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)
}
