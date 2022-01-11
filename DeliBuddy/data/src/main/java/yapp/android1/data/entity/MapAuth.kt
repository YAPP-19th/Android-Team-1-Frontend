package yapp.android1.data.entity

import yapp.android1.domain.entity.AuthEntity

object MapAuth {
    fun map(authModel: AuthModel): AuthEntity {
        authModel.apply {
            return AuthEntity(
                token = jwt,
                userId = userId
            )
        }
    }
}