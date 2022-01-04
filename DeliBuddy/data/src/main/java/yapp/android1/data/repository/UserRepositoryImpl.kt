package yapp.android1.data.repository

import yapp.android1.data.entity.UserModel
import yapp.android1.data.remote.UserApi
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.UserEntity
import yapp.android1.domain.interactor.DeliBuddyNetworkErrorHandler
import yapp.android1.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val deliBuddyNetworkErrorHandler: DeliBuddyNetworkErrorHandler,
) : UserRepository {
    override suspend fun getMyInfo(): NetworkResult<UserEntity> {
        return try {
            val response = api.getMyInfo()

            NetworkResult.Success(
                UserModel.toUserEntity(response)
            )
        } catch (exception: Exception) {
            val error = deliBuddyNetworkErrorHandler.getError(exception)
            NetworkResult.Error(error)
        }
    }

    override suspend fun setFcmToken(token: String): NetworkResult<Unit> {
        return try {
            //지히님 편한대로 수정하셔요!
            api.setFcmToken()
            NetworkResult.Success(Unit)
        } catch (exception: Exception) {
            val error = deliBuddyNetworkErrorHandler.getError(exception)
            NetworkResult.Error(error)
        }
    }
}
