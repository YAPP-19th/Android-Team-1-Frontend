package yapp.android1.domain.repository

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.UserEntity

interface UserRepository {
    suspend fun getMyInfo(): NetworkResult<UserEntity>
    suspend fun setFcmToken(token: String): NetworkResult<Unit>
}
