package com.mrkaz.tokoin.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mrkaz.tokoin.data.database.dao.ReferenceDAO
import com.mrkaz.tokoin.data.database.entity.ReferenceEntity


@Database(entities = [ReferenceEntity::class], version = 1)
abstract class ReferenceDatabase : RoomDatabase() {

    abstract fun referenceDAO(): ReferenceDAO
}