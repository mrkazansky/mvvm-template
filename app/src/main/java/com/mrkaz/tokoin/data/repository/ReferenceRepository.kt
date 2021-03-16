package com.mrkaz.tokoin.data.repository

import com.mrkaz.tokoin.data.database.ReferenceDatabase
import com.mrkaz.tokoin.data.database.entity.ReferenceEntity
import org.koin.core.KoinComponent
import org.koin.core.inject

class ReferenceRepository : KoinComponent {

    val referenceDatabase: ReferenceDatabase by inject()

    suspend fun getAll(): List<ReferenceEntity> {
        return referenceDatabase.referenceDAO().getAll()
    }

    suspend fun insert(items: List<ReferenceEntity>) {
        referenceDatabase.referenceDAO().insert(items)
    }
}