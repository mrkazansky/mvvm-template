package com.mrkaz.tokoin.data.database.dao

import androidx.room.*
import com.mrkaz.tokoin.data.database.entity.ReferenceEntity

@Dao
interface ReferenceDAO {

    @Query("SELECT * FROM `references`")
    fun getAll(): List<ReferenceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<ReferenceEntity>)

}