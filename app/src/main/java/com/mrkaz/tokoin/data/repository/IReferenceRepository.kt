package com.mrkaz.tokoin.data.repository

import com.mrkaz.tokoin.data.database.entity.ReferenceEntity

interface IReferenceRepository {

    suspend fun getAll(): List<ReferenceEntity>

    suspend fun insert(items: List<ReferenceEntity>)

}