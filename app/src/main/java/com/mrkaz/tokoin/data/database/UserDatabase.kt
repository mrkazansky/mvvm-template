package com.mrkaz.tokoin.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mrkaz.tokoin.data.database.dao.UserDAO
import com.mrkaz.tokoin.data.database.entity.UserEntity


@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDAO(): UserDAO
}