package yapp.android1.domain.repository

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.AuthEntity

interface AuthRepository {
    suspend fun fetchAuthToken(token: String): NetworkResult<AuthEntity>
}