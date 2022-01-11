package yapp.android1.data.repository

import yapp.android1.data.entity.MapAuth
import yapp.android1.data.remote.AuthApi
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.AuthEntity
import yapp.android1.domain.interactor.DeliBuddyNetworkErrorHandler
import yapp.android1.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val deliBuddyNetworkErrorHandler: DeliBuddyNetworkErrorHandler,
) : AuthRepository {

    override suspend fun fetchAuthToken(token: String): NetworkResult<AuthEntity> {
        return try {
            val response = api.fetchAuthToken(token)

            NetworkResult.Success(
                MapAuth.map(response)
            )
        } catch (exception: Exception) {
            val error = deliBuddyNetworkErrorHandler.getError(exception)
            NetworkResult.Error(error)
        }

    }

    override suspend fun refreshAuthToken(): NetworkResult<AuthEntity> {
        return try {
            val response = api.refreshAuthToken()

            NetworkResult.Success(
                MapAuth.map(response)
            )
        } catch (exception: Exception) {
            val error = deliBuddyNetworkErrorHandler.getError(exception)
            NetworkResult.Error(error)
        }
    }
}