package com.mrkaz.tokoin.data.database.dao

import androidx.room.*
import com.mrkaz.tokoin.data.database.entity.UserEntity

@Dao
interface UserDAO {
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    fun login(username: String, password: String): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)

}