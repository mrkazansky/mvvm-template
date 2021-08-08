package com.mrkaz.tokoin.data.repository.impl

import com.mrkaz.tokoin.data.database.ReferenceDatabase
import com.mrkaz.tokoin.data.database.entity.ReferenceEntity
import com.mrkaz.tokoin.data.repository.IReferenceRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class ReferenceRepository : IReferenceRepository, KoinComponent {

    val referenceDatabase: ReferenceDatabase by inject()

    override suspend fun getAll(): List<ReferenceEntity> {
        return referenceDatabase.referenceDAO().getAll()
    }

    override suspend fun insert(items: List<ReferenceEntity>) {
        referenceDatabase.referenceDAO().insert(items)
    }
}