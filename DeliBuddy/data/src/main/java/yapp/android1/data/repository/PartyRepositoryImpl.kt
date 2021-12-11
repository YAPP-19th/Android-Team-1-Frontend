package yapp.android1.data.repository

import yapp.android1.data.entity.PartyModel
import yapp.android1.data.remote.PartyApi
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.PartyEntity
import yapp.android1.domain.interactor.DeliBuddyNetworkErrorHandler
import yapp.android1.domain.repository.PartyRepository
import javax.inject.Inject

class PartyRepositoryImpl @Inject constructor(
    private val api: PartyApi,
    private val deliBuddyNetworkErrorHandler: DeliBuddyNetworkErrorHandler,
) : PartyRepository {
    override suspend fun getPartiesInCircle(point: String, distance: Int): NetworkResult<List<PartyEntity>> {
        return try {
            val response = api.getPartiesInCircle(point, distance)

            NetworkResult.Success(
                response.map {
                    PartyModel.toPartyEntity(it)
                }
            )
        } catch (e: Exception) {
            val errorType = deliBuddyNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }

    override suspend fun getPartiesInGeom(): NetworkResult<Unit> {
        return try {
            val response = api.getPartiesInGeom()
            NetworkResult.Success(response)
        } catch (e: Exception) {
            val errorType = deliBuddyNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }
}
