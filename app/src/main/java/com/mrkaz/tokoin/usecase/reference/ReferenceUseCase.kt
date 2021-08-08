package com.mrkaz.tokoin.usecase.reference

import com.mrkaz.tokoin.common.utils.AuthUtils
import com.mrkaz.tokoin.data.database.entity.ReferenceEntity
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.data.repository.IAuthRepository
import com.mrkaz.tokoin.data.repository.IReferenceRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class ReferenceUseCase : KoinComponent {

    val authRepository: IAuthRepository by inject()
    val referenceRepository: IReferenceRepository by inject()
    val authUtils: AuthUtils by inject()

    suspend fun updateUserReference(userEntity: UserEntity, reference: String) {
        if (userEntity.reference != reference) {
            userEntity.reference = reference
            authUtils.updateUserdata(userEntity)
            authRepository.update(userEntity)
        }
    }

    suspend fun getAll(): List<ReferenceEntity> {
        return referenceRepository.getAll()
    }

    suspend fun insert(items: List<ReferenceEntity>) {
        referenceRepository.insert(items)
    }

}